package com.example.concentriccirclesidewiseview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#EF5350",
    "#AA00FF",
    "#C51162",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val circles : Int = 3
val parts : Int = circles + 2
val delay : Long = 20
val deg : Float = 45f
val sizeFactor : Float = 4.9f
val strokeFactor : Float = 90f
val backColor : Int = Color.parseColor("#BDBDBD")
val divideFactor : Float = 1.4f
val scGap : Float = 0.05f / parts

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawConcentricCircleSideWise(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(circles, parts)
    val sc2 : Float = scale.divideScale(circles + 1, parts)
    save()
    translate(w / 2 , h / 2 + (h / 2 + size * circles) * sc2)
    rotate(deg * sc2 )
    for (k in 0..1) {
        save()
        rotate(deg * (1f - 2 * k) * sc1)
        for (j in 0..1) {
            var x : Float = 0f
            var barSize : Float = size
            save()
            scale(1f - 2 * j, 1f)
            for (i in 0..(circles - 1)) {
                save()
                translate(x, 0f)
                drawArc(
                    RectF(-barSize / 2, -barSize / 2, barSize / 2, barSize / 2),
                    0f,
                    360f * scale.divideScale(i, parts),
                    false,
                    paint
                )
                restore()
                x += (barSize / 2 + barSize / (2 * divideFactor))
                barSize /= divideFactor
            }
            restore()
        }
        restore()
    }
    restore()
}

fun Canvas.drawCCSWNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.style = Paint.Style.STROKE
    drawConcentricCircleSideWise(scale, w, h, paint)
}

class ConcentricCircleSideWiseView(ctx : Context) : View(ctx) {

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

    data class State(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

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

    data class CCSWNode(var i : Int, val state : State = State()) {

        private var prev : CCSWNode? = null
        private var next : CCSWNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = CCSWNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawCCSWNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : CCSWNode {
            var curr : CCSWNode? = prev
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

    data class ConcentricCircleSideWise(var i : Int) {

        private var curr : CCSWNode = CCSWNode(0)
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

    data class Renderer(var view : ConcentricCircleSideWiseView) {

        private val animator : Animator = Animator(view)
        private val ccsw : ConcentricCircleSideWise = ConcentricCircleSideWise(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            ccsw.draw(canvas, paint)
            animator.animate {
                ccsw.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            ccsw.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : ConcentricCircleSideWiseView {
            val view : ConcentricCircleSideWiseView = ConcentricCircleSideWiseView(activity)
            activity.setContentView(view)
            return view
        }
    }
}