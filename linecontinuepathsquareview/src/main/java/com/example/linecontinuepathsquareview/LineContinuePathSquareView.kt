package com.example.linecontinuepathsquareview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Color
import android.graphics.RectF
import android.graphics.Canvas

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#EF5350",
    "#AA00FF",
    "#C51162",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val delay : Long = 20
val lines : Int = 3
val parts : Int =  lines + 2
val scGap : Float = 0.05f / parts
val deg : Float = 90f
val backColor : Int = Color.parseColor("#BDBDBD")
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.5f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawLineContinuePathSquare(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    save()
    translate(w / 2, h / 2 - (h / 2 + size / 2) * sc5)
    for (j in 0..(lines - 1)) {
        val scj : Float = scale.divideScale(j, parts)
        save()
        rotate(deg * j)
        drawLine(
            size / 2,
            size / 2,
            size / 2,
            size / 2 - size * scj,
            paint
        )
        restore()
    }
    save()
    translate(0f, -(h / 2 + size / 2) * (1 - sc4))
    drawRect(RectF(-size / 2, -size / 2, size / 2, size / 2), paint)
    restore()
    restore()
}

fun Canvas.drawLCPSNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawLineContinuePathSquare(scale, w, h, paint)
}

class LineContinuePathSquareView(ctx : Context) : View(ctx) {

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
            scale += scGap * this.dir
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

    data class LCPSNode(var i : Int, val state : State = State()) {

        private var next : LCPSNode? = null
        private var prev : LCPSNode? = null

        init {

        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = LCPSNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawLCPSNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : LCPSNode {
            var curr : LCPSNode? = prev
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

    data class LineContinuePathSquare(var i : Int) {

        private var curr : LCPSNode = LCPSNode(0)
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

    data class Renderer(var view : LineContinuePathSquareView) {

        private val animator : Animator = Animator(view)
        private val lcs : LineContinuePathSquare = LineContinuePathSquare(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            lcs.draw(canvas, paint)
            animator.animate {
                lcs.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            lcs.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity: Activity) : LineContinuePathSquareView {
            val view : LineContinuePathSquareView = LineContinuePathSquareView(activity)
            activity.setContentView(view)
            return view

        }
    }
}