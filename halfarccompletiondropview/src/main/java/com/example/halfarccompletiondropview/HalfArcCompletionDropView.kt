package com.example.halfarccompletiondropview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Color

val colors : Array<Int> = arrayOf(
    "#304FFE",
    "#C51162",
    "#F57F17",
    "#00C853",
    "#FFD600"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 4
val scGap : Float = 0.04f / parts
val sizeFactor : Float = 4.9f
val strokeFactor : Float = 90f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 180f
val start : Float = -90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawHalfArcCompletionDrop(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    save()
    translate(w / 2, h / 2 + (h / 2 + size / 2)  * sc4)
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f)
        save()
        translate((w / 2 - size / 2) * (1 - sc3), 0f)
        drawArc(
            RectF(-size / 2, -size / 2, size / 2, size / 2),
            start,
            deg * sc1,
            false,
            paint
        )
        restore()
        if (sc2 > 0f) {
            for (k in 0..1) {
                save()
                translate(0f, -size / 2 + size * k)
                drawLine(0f, 0f, (w / 2 - size / 2) * (sc2 - sc3), 0f, paint)
                restore()
            }
        }
        restore()
    }
    restore()
}

fun Canvas.drawHACDNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.style = Paint.Style.STROKE
    drawHalfArcCompletionDrop(scale, w, h, paint)
}

class HalfArcCompletionDropView(ctx : Context) : View(ctx) {

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
            if (Math.abs(scale - prevScale) > 1) {
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

    data class HACDNode(val i : Int, val state : State = State()) {

        private var next : HACDNode? = null
        private var prev : HACDNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = HACDNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawHACDNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : HACDNode {
            var curr : HACDNode? = prev
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

    data class HalfArcCompletionDrop(var i : Int) {

        private var curr : HACDNode = HACDNode(0)
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

    data class Renderer(var view : HalfArcCompletionDropView) {

        private val animator : Animator = Animator(view)
        private val hacd : HalfArcCompletionDrop = HalfArcCompletionDrop(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            hacd.draw(canvas, paint)
            animator.animate {
                hacd.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            hacd.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : HalfArcCompletionDropView {
            val view : HalfArcCompletionDropView = HalfArcCompletionDropView(activity)
            activity.setContentView(view)
            return view
        }
    }
}