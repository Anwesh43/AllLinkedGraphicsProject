package com.example.ballsdropintosweepview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Color
import android.graphics.Canvas
import android.app.Activity
import android.content.Context

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#EF5350",
    "#AA00FF",
    "#C51162",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val sizeFactor : Float = 5.3f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val balls : Int = 3
val parts : Int = balls + 2
val scGap : Float = 0.05f / parts

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawBallsDropIntoSweep(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    save()
    translate(w / 2, h / 2)
    for (j in 0..(balls - 1)) {
        val scj : Float = scale.divideScale(j, parts)
        save()
        translate((-w / 4 + (w / 4) * j) * (1 - sc4), (-h / 2 - size / 2) * (1 - scj))
        drawArc(
            RectF(
                -size / 2, -size / 2, size / 2, size / 2
            ),
            360f * sc5,
            360f * (1 - sc5),
            true,
            paint
        )
        restore()
    }
    restore()
}

fun Canvas.drawBDISNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    drawBallsDropIntoSweep(scale, w, h, paint)
}

class BallsDropIntoSweepView(ctx : Context) : View(ctx) {

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

    data class BDISNode(var i : Int, val state : State = State()) {

        private var next : BDISNode? = null
        private var prev : BDISNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = BDISNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawBDISNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : BDISNode {
            var curr : BDISNode? = prev
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

    data class BallsDropIntoSweep(var i : Int) {

        private var curr : BDISNode = BDISNode(0)
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

    data class Renderer(var view : BallsDropIntoSweepView) {

        private val animator : Animator = Animator(view)
        private val bdis : BallsDropIntoSweep = BallsDropIntoSweep(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            bdis.draw(canvas, paint)
            animator.animate {
                bdis.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            bdis.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity: Activity) : BallsDropIntoSweepView {
            val view : BallsDropIntoSweepView = BallsDropIntoSweepView(activity)
            activity.setContentView(view)
            return view
        }
    }
}