package com.example.downloadiconmoveview

import android.view.View
import android.view.MotionEvent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Canvas
import android.app.Activity
import android.content.Context

val colors : Array<Int> = arrayOf(
    "#D32F2F",
    "#006064",
    "#01579B",
    "#BF360C",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 5
val scGap : Float = 0.04f / parts
val strokeFactor : Float = 90f
val deg : Float = 45f
val rot : Float = 90f
val sizeFactor : Float = 4.9f
val arrowSizeFactor : Float = 5.2f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawDownloadIconMove(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    val arrowSize : Float = size / arrowSizeFactor
    save()
    translate(w / 2, h / 2)
    save()
    rotate(-rot * sc4)
    translate(0f, (h / 2 + size) * sc5)
    if (sc1 > 0f) {
        drawLine(0f, -size * 0.5f * sc1, 0f, size * 0.5f * sc1, paint)
    }
    save()
    translate(0f, size / 2)
    for (j in 0..1) {
        save()
        rotate(deg * sc3 * (1f - 2 * j))
        if (Math.floor(sc1.toDouble()).toFloat() > 0f) {
            drawLine(
                0f,
                0f,
                0f,
                -arrowSize * Math.floor(sc1.toDouble()).toFloat(),
                paint
            )
        }
        restore()
    }
    restore()
    restore()
    save()
    translate(0f, size / 2 + (h / 2 + size / 2) * sc4)
    if (sc2 > 0f) {
        drawLine(-size * 0.25f * sc2, 0f, size * 0.25f * sc2, 0f, paint)
    }
    restore()

    restore()
}

fun Canvas.drawDIMNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawDownloadIconMove(scale, w, h, paint)
}

class DownloadIconMoveView(ctx : Context) : View(ctx) {

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1f) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class DIMNode(var i : Int, val state : State = State()) {

        private var next : DIMNode? = null
        private var prev : DIMNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = DIMNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawDIMNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : DIMNode {
            var curr : DIMNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class DownloadIconMove(var i : Int, val state : State = State()) {

        private var curr : DIMNode = DIMNode(0)
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : DownloadIconMoveView) {

        private val animator : Animator = Animator(view)
        private val dim : DownloadIconMove = DownloadIconMove(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            dim.draw(canvas, paint)
            animator.animate {
                dim.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            dim.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : DownloadIconMoveView {
            val view : DownloadIconMoveView = DownloadIconMoveView(activity)
            activity.setContentView(view)
            return view
        }
    }
}