package com.example.direclinescreateview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
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
val parts : Int = 4
val scGap : Float = 0.03f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 5.2f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawEndStartLine(scale : Float, i : Int, gap : Int, x : Float, y : Float, paint : Paint) {
    val scStart : Float = scale.divideScale(i, parts)
    val scEnd : Float = scale.divideScale(i + gap, parts)
    drawLine(x * scEnd, y * scEnd, x * scStart, y * scEnd, paint)
}

fun Canvas.drawDirecLinesCreate(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    save()
    translate(w / 2, h / 2)
    save()
    translate(0f, -h / 2)
    drawEndStartLine(scale, 1, 2, 0f, h, paint)
    restore()
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f)
        translate(w / 2 - size, h / 2)
        drawEndStartLine(scale, 0, 2, size, -size, paint)
        restore()
    }
    restore()
}

fun Canvas.drawDLCNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawDirecLinesCreate(scale, w, h, paint)
}

class DirecLinesCreateView(ctx : Context) : View(ctx) {

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

    data class DLCNode(var i : Int, val state : State = State()) {

        private var next : DLCNode? = null
        private var prev : DLCNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = DLCNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawDLCNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : DLCNode {
            var curr : DLCNode? = prev
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

    data class DirecLinesCreate(var i : Int, val state : State = State()) {

        private var curr : DLCNode = DLCNode(0)
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
            state.startUpdating(cb)
        }
    }

    data class Renderer(var view : DirecLinesCreateView) {

        private val animator : Animator = Animator(view)
        private val dlc : DirecLinesCreate = DirecLinesCreate(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            dlc.draw(canvas, paint)
            animator.animate {
                dlc.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            dlc.startUpdating {
                animator.start()
            }
        }
    }
}