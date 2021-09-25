package com.example.squarebreakthenjoinview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Canvas
import android.graphics.Color
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
val parts : Int = 5
val scGap : Float = 0.05f / parts
val sizeFactor : Float = 4.9f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawSquareBreakThenJoin(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    save()
    translate(w / 2, h / 2 + (h / 2 + size / 2) * sc5)
    rotate(deg * sc3)
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f - 2 * j)
        translate((size / 2) * (sc2 - sc4), 0f)
        drawRect(
            RectF(
                -size / 2,
                -size / 2,
                size / 2,
                -size / 2 + size * 0.5f * sc1),
            paint
        )
        restore()
    }
    restore()
}

fun Canvas.drawSBTJNode(i : Int, scale : Float, paint : Paint) {
    paint.color = colors[i]
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    drawSquareBreakThenJoin(scale, w, h, paint)
}

class SquareBreakThenJoinView(ctx : Context) : View(ctx) {

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
                dir =  1f - 2 * prevScale
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

    data class SBTJNode(var i : Int, val state : State = State()) {

        private var next : SBTJNode? = null
        private var prev : SBTJNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = SBTJNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawSBTJNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb: () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : SBTJNode {
            var curr : SBTJNode? = prev
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

    data class SquareBreakThenJoin(var i : Int) {

        private var curr : SBTJNode = SBTJNode(0)
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

    data class Renderer(var view : SquareBreakThenJoinView) {

        private val animator : Animator = Animator(view)
        private val sbtj : SquareBreakThenJoin = SquareBreakThenJoin(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            sbtj.draw(canvas, paint)
            animator.animate {
                sbtj.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            sbtj.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : SquareBreakThenJoinView {
            val view : SquareBreakThenJoinView = SquareBreakThenJoinView(activity)
            activity.setContentView(view)
            return view
        }
    }
}