package com.example.dividelinearrowdownview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Color
import android.graphics.Canvas
import android.content.Context
import android.app.Activity

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
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 45f
val rot : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawDivideLineArrowDown(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    save()
    translate(w / 2 + (w / 2 + size) * sc4, h / 2)
    rotate(-rot * sc3)
    for (j in 0..1) {
        save()
        scale(1f, 1f - 2 * j)
        for (k in 0..1) {
            save()
            rotate(rot * sc2 * j * (1f - 2 * k))
            drawLine(0f, 0f, 0f, size * sc1, paint)
            restore()
        }
        restore()
    }
    restore()
}

fun Canvas.drawDLADNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawDivideLineArrowDown(scale, w, h, paint)
}

class DivideLineArrowDownView(ctx : Context) : View(ctx) {

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

    data class DLADNode(var i : Int, val state : State = State()) {

        private var next : DLADNode? = null
        private var prev : DLADNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = DLADNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawDLADNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : DLADNode {
            var curr : DLADNode? = prev
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

    data class DivideLineArrowDown(var i : Int) {

        private var curr : DLADNode = DLADNode(0)
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

    data class Renderer(var view : DivideLineArrowDownView) {

        private val animator : Animator = Animator(view)
        private val dlad : DivideLineArrowDown= DivideLineArrowDown(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            dlad.draw(canvas, paint)
            animator.animate {
                dlad.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            dlad.startUpdating {
                animator.start()
            }
        }
    }
}