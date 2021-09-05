package com.example.trilinecreateendview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Canvas

val colors : Array<Int> = arrayOf(
    "#304FFE",
    "#C51162",
    "#F57F17",
    "#00C853",
    "#FFD600"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 6
val scGap : Float = 0.06f / parts
val strokeFactor : Float = 90f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawUpdateLine(x : Float, y : Float, sizeX : Float, sizeY : Float, sc : Float, j : Int, paint : Paint) {
    val x1 : Float = x + sizeX * sc.divideScale(3 + j, parts)
    val y1 : Float = y + sizeY * sc.divideScale(3 + j, parts)
    val x2 : Float = x + sizeX * sc.divideScale(j, parts)
    val y2 : Float = y + sizeY * sc.divideScale(j, parts)
    drawLine(x1, y1, x2, y2, paint)
}

fun Canvas.drawTriLineCreateEnd(scale : Float, w : Float, h : Float, paint : Paint) {
    save()
    translate(w / 2, h / 2)
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f - 2 * j)
        drawUpdateLine(w / 2, 0f, -w / 2, 0f, scale, 0, paint)
        drawUpdateLine(0f, 0f, 0f, -h / 2, scale, 1, paint)
        drawUpdateLine(0f, -h / 2, w / 2, h / 2, scale, 2, paint)
        restore()
    }
    restore()
}

fun Canvas.drawTLCENode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawTriLineCreateEnd(scale, w, h, paint)
}

class TriLineCreateEndView(ctx : Context) : View(ctx) {

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas: Canvas) {
        renderer.render(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale: Float = 0f, var dir: Float = 0f, var prevScale: Float = 0f) {

        fun update(cb: (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb: () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view: View, var animated: Boolean = false) {

        fun animate(cb: () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch (ex: Exception) {

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

    data class TLCENode(var i: Int, val state: State = State()) {

        private var next: TLCENode? = null
        private var prev: TLCENode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = TLCENode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas: Canvas, paint: Paint) {
            canvas.drawTLCENode(i, state.scale, paint)
        }

        fun update(cb: (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb: () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir: Int, cb: () -> Unit): TLCENode {
            var curr: TLCENode? = prev
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

    data class TriLineCreateEnd(var i: Int) {

        private var curr: TLCENode = TLCENode(0)
        private var dir: Int = 1

        fun draw(canvas: Canvas, paint: Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb: (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(it)
            }
        }

        fun startUpdating(cb: () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : TriLineCreateEndView) {

        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val animator : Animator = Animator(view)
        private val tlce : TriLineCreateEnd = TriLineCreateEnd(0)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            tlce.draw(canvas, paint)
            animator.animate {
                tlce.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            tlce.startUpdating {
                animator.start()
            }
        }
    }
}