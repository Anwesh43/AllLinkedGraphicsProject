package com.example.breakbarrisecircleview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Canvas
import android.graphics.Color

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#00C853",
    "#AA00FF",
    "#01579B",
    "#BF360C"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 4
val scGap : Float = 0.04f / parts
val delay : Long = 20
val rot : Float = 90f
val sizeFactor : Float = 4.9f
val strokeFactor : Float = 90f
val deg : Float = 180f
val backColor : Int = Color.parseColor("#BDBDBD")
val barWFactor : Float = 5.9f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawBreakBarRiseCircle(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val barW : Float = size / barWFactor
    paint.style = Paint.Style.FILL
    save()
    translate(w / 2, h / 2)
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f)
        translate((w / 2) * sc4, 0f)
        rotate(rot * sc2)
        drawRect(RectF(0f, -size * sc1, barW, 0f), paint)
        restore()
    }
    paint.style = Paint.Style.STROKE
    save()
    //translate(0f, -barW)
    drawArc(
        RectF(
            -size / 2,
            -size / 2,
            size / 2,
            size / 2
        ),
        deg + deg * sc4,
        deg * (sc3 - sc4),
        false,
        paint
    )
    restore()
    restore()
}

fun Canvas.drawBBRCNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawBreakBarRiseCircle(scale, w, h, paint)
}

class BreakBarIsRiseCircleView(ctx : Context) : View(ctx) {
    val renderer : Renderer = Renderer(this)
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

    data class BBRCNode(var i : Int, val state : State = State()) {

        private var prev : BBRCNode? = null
        private var next : BBRCNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = BBRCNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawBBRCNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : BBRCNode {
            var curr : BBRCNode? = prev
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

    data class BreakBarRiseCircle(var i : Int) {

        private var curr : BBRCNode = BBRCNode(0)
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

    data class Renderer(var view : BreakBarIsRiseCircleView) {

        private val animator : Animator = Animator(view)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val bbrc : BreakBarRiseCircle = BreakBarRiseCircle(0)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            bbrc.draw(canvas, paint)
            animator.animate {
                bbrc.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            bbrc.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity) : BreakBarIsRiseCircleView {
            val view : BreakBarIsRiseCircleView = BreakBarIsRiseCircleView(activity)
            activity.setContentView(view)
            return view
        }
    }
}