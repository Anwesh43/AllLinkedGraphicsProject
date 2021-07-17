package com.example.createsteptomoveview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Canvas

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#EF5350",
    "#AA00FF",
    "#C51162",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val lines : Int = 3
val parts : Int = lines + 4
val scGap : Float = 0.07f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val rot : Float = 180f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawCreateStepToMove(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    val sc6 : Float = scale.divideScale(5, parts)
    val sc7 : Float = scale.divideScale(6, parts)
    val xGap : Float = size / lines
    save()
    translate(w / 2 + (w / 2 + size / 2) * sc7, h / 2 + (h / 2 - size / 2) * (1 - sc6))
    rotate(rot * sc7)
    for (j in 0..(lines - 1)) {
        val scj : Float = scale.divideScale(j, parts)
        val scj1 : Float = scj.divideScale(0, 2)
        val scj2 : Float = scj.divideScale(1, 2)
        save()
        translate(-size / 2 + xGap * j, size / 2 - xGap * j)
        if (scj1 > 0f) {
            drawLine(0f, 0f, 0f, -xGap * scj1, paint)
        }
        if (scj2 > 0f) {
            drawLine(0f, -xGap, xGap * scj2, -xGap, paint)
        }
        restore()
    }
    if (sc4 > 0f) {
        drawLine(
            size / 2,
            -size / 2,
            size / 2,
            -size / 2 + size * sc4,
            paint
        )
    }
    if (sc5 > 0f) {
        drawLine(
            size / 2,
            size / 2,
            size / 2 - size * sc5,
            size / 2,
            paint
        )
    }
    restore()
}

fun Canvas.drawCSTMNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawCreateStepToMove(scale, w, h, paint)
}

class CreateStepToMoveView(ctx : Context) : View(ctx) {

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

    data class CSTMNode(var i : Int, val state : State = State()) {

        private var next : CSTMNode? = null
        private var prev : CSTMNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = CSTMNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawCSTMNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : CSTMNode {
            var curr : CSTMNode? = prev
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

    data class CreateStepToMove(var i : Int) {
        var curr : CSTMNode = CSTMNode(0)
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

    data class Renderer(var view : CreateStepToMoveView) {

        private val animator : Animator = Animator(view)
        private val cstm : CreateStepToMove = CreateStepToMove(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            cstm.draw(canvas, paint)
            animator.animate {
                cstm.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            cstm.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : CreateStepToMoveView {
            val view : CreateStepToMoveView = CreateStepToMoveView(activity)
            activity.setContentView(view)
            return view
        }
    }
}