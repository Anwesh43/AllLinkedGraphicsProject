package com.example.barbelowlineview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Color
import android.graphics.Canvas

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#00C853",
    "#AA00FF",
    "#01579B",
    "#BF360C"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 6
val scGap : Float = 0.04f / parts
val delay : Long = 20
val sizeFactor : Float = 4.9f
val strokeFactor : Float =  90f
val rot : Float = 90f
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawBarBelowLine(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc5 : Float = scale.divideScale(4, parts)
    val sc6 : Float = scale.divideScale(5, parts)
    val barSize : Float = size / 4
    save()
    translate(w / 2, h / 2 + (h / 2 + size) * sc6)
    rotate(rot * sc5)
    for (j in 0..1) {
        val sc1 : Float = scale.divideScale(j * 2, parts)
        val sc2 : Float = scale.divideScale(j * 2 + 1, parts)
        save()
        scale(1f - 2 * j, 1f)
        translate(-size / 2, 0f)
        drawRect(RectF(0f, -barSize * sc1, barSize, 0f), paint)
        drawLine(barSize / 2 - (barSize / 2) * sc2, -size / 2, barSize / 2 + (barSize / 2) * sc2, -size / 2, paint)
        restore()
    }
    restore()
}

fun Canvas.drawBBLNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawBarBelowLine(scale, w, h, paint)
}

class BarBelowLineView(ctx : Context) : View(ctx) {

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

    data class BBLNode(var i : Int, val state : State = State()) {

        private var prev : BBLNode? = null
        private var next : BBLNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = BBLNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawBBLNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : BBLNode {
            var curr : BBLNode? = prev
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

    data class BarBelowLine(var i : Int, val state : State = State()) {

        private var curr : BBLNode = BBLNode(0)
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

    data class Renderer(var view : BarBelowLineView) {

        private val animator : Animator = Animator(view)
        private val bbl : BarBelowLine = BarBelowLine(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            bbl.draw(canvas, paint)
            animator.animate {
                bbl.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            bbl.startUpdating {
                animator.start()
            }
        }
    }
}