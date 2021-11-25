package com.example.lineexpandrotatorview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.app.Activity
import android.content.Context

val colors : Array<Int> = arrayOf(
    "#2962FF",
    "#BF360C",
    "#00C853",
    "#C51162",
    "#FFD600"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 5
val scGap : Float = 0.04f / parts
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val rot : Float = 180f
val sizeFactor : Float = 4.9f
val strokeFactor : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawLineExpandRotator(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    save()
    translate(w / 2, h / 2 + (h / 2 + paint.strokeWidth) * sc5)
    rotate(rot * sc4)
    for (j in 0..2) {
        save()
        translate((-size / 2 + size * 0.5f * j) * sc3, 0f)
        drawLine(0f, 0f, 0f, -size * sc2, paint)
        restore()
    }
    drawLine(-size * 0.5f * sc1, 0f, size * 0.5f * sc1, 0f, paint)
    restore()
}

fun Canvas.drawLERNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawLineExpandRotator(scale, w, h, paint)
}

class LineExpandRotatorView(ctx : Context) : View(ctx) {

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
                view.invalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class LERNode(var i : Int, val state : State = State()) {

        private var prev : LERNode? = null
        private var next : LERNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = LERNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawLERNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : LERNode {
            var curr : LERNode? = prev
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

    data class LineExpandRotator(var i : Int) {

        private var curr : LERNode = LERNode(0)
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

    data class Renderer(var view : LineExpandRotatorView) {

        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val animator : Animator = Animator(view)
        private val ler : LineExpandRotator = LineExpandRotator(0)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            ler.draw(canvas, paint)
            animator.animate {
                ler.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            ler.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : LineExpandRotatorView {
            val view : LineExpandRotatorView = LineExpandRotatorView(activity)
            activity.setContentView(view)
            return view
        }
    }
}