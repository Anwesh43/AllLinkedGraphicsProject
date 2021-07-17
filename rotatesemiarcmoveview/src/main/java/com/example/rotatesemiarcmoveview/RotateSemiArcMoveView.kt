package com.example.rotatesemiarcmoveview

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
val parts : Int = 4
val scGap : Float = 0.02f / parts
val strokeFactor : Float = 90f
val delay : Long = 20
val sizeFactor : Float = 4.9f
val backColor : Int = Color.parseColor("#BDBDBD")
val rot : Float = 180f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int , n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawRotateSemiArcMove(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    save()
    translate(w / 2 + (w / 2 + size / 2) * sc4, h / 2)
    rotate(rot * sc4)
    save()
    translate(0f, (h / 2 - size / 2) * (1 - sc2))
    rotate(rot * sc2)
    drawArc(
        RectF(
            -size / 2,
            -size / 2,
            size / 2,
            size / 2
        ), 0f, rot * sc1, false, paint)
    restore()
    drawArc(
        RectF(
            -size / 2,
            -size / 2,
            size / 2,
            size / 2
        ), 0f, rot * sc3, false, paint)
    restore()
}

fun Canvas.drawRSAMNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawRotateSemiArcMove(scale, w, h, paint)
}

class RotateSemiArcMoveView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += dir * scGap
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

        data class Animator(var view : View, var animated : Boolean = false) {

            fun animate(cb : () -> Unit) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

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

        data class RSAMNode(var i : Int, val state : State = State()) {

            private var next : RSAMNode? = null
            private var prev : RSAMNode? = null

            init {
                addNeighbor()
            }

            fun addNeighbor() {
                if (i < colors.size - 1) {
                    next = RSAMNode(i + 1)
                    next?.prev = this
                }
            }

            fun draw(canvas : Canvas, paint : Paint) {
                canvas.drawRSAMNode(i, state.scale, paint)
            }

            fun update(cb : (Float) -> Unit) {
                state.update(cb)
            }

            fun startUpdating(cb : () -> Unit) {
                state.startUpdating(cb)
            }

            fun getNext(dir : Int, cb : () -> Unit) : RSAMNode {
                var curr : RSAMNode? = prev
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
    }
}