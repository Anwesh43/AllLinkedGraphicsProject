package com.example.concarclinejoinerview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Color

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#EF5350",
    "#AA00FF",
    "#C51162",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 4
val concs : Int = 2
val scGap : Float = 0.08f / (parts * concs)
val delay : Long = 20
val r1Factor : Float = 5.2f
val r2Factor : Float = 3.2f
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 90f
val strokeFactor : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawConcArcLineJoin(scale : Float, w : Float, h : Float, paint : Paint) {
    val r1 : Float = Math.min(w, h) / r1Factor
    val r2 : Float = Math.min(w, h) / r2Factor
    val sf : Float = scale.sinify()

    save()
    translate(w / 2, h / 2)
    for (j in 0..(parts - 1)) {
        val sfj : Float = sf.divideScale(j, parts)
        val sfj1 : Float = sfj.divideScale(0, concs)
        val sfj2 : Float = sfj.divideScale(1, concs)
        var r : Float = r1 * (1 - (j % 2)) + r2 * (j % 2)
        val yStart : Float = r1 * (1 - (j % 2)) + r2 * (j % 2)
        val yEnd : Float = r2 * (1 -  j % 2) + r1 * (j % 2)
        save()
        rotate(deg * j)
        if (sfj1 > 0f) {
            drawArc(RectF(-r, -r, r, r), 0f, deg * sfj1, false, paint)
        }
        if (sfj2 > 0f) {
            drawLine(0f, yStart, 0f, yStart + (yEnd - yStart) * sfj2, paint)
        }
        restore()
    }
    restore()
}

fun Canvas.drawCALJNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.style = Paint.Style.STROKE
    drawConcArcLineJoin(scale, w, h, paint)
}

class ConcArcLineJoinerView(ctx : Context) : View(ctx) {

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

    data class CALJNode(var i : Int, val state : State = State()) {

        private var next : CALJNode? = null
        private var prev : CALJNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = CALJNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawCALJNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : CALJNode {
            var curr : CALJNode? = prev
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

    data class ConcArcLineJoiner(var i : Int) {

        private var curr : CALJNode = CALJNode(0)
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

    data class Renderer(var view : ConcArcLineJoinerView) {

        private val animator : Animator = Animator(view)
        private val calj : ConcArcLineJoiner = ConcArcLineJoiner(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            calj.draw(canvas, paint)
            animator.animate {
                calj.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            calj.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : ConcArcLineJoinerView {
            val view : ConcArcLineJoinerView = ConcArcLineJoinerView(activity)
            activity.setContentView(view)
            return view
        }
    }
}