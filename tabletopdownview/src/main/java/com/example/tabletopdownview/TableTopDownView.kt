package com.example.tabletopdownview

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
    "#00C853",
    "#AA00FF",
    "#01579B",
    "#BF360C"
).map {
    Color.parseColor(it)
}.toTypedArray()
val strokeFactor : Float = 90f
val sizeFactor : Float = 5.9f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val rot : Float = 270f
val parts : Int = 4
val scGap : Float = 0.04f / parts
val barHFactor : Float = 7.2f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawTableTopDown(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val barH : Float = size / barHFactor
    save()
    translate(w / 2, h / 2)
    drawRect(RectF(-size / 2, -barH, -size / 2 + size * sc3, 0f), paint)
    for (j in 0..2) {
        val k : Float = 1f - j % 2
        val sk : Float = 1f - k
        val x1 : Float = k * Math.floor(sc1.toDouble()).toFloat() * size * (1 - j)
        val x2 : Float = sk * sc1 * size
        save()
        translate(-size / 2 + size * 0.5f * j, 0f)
        rotate(rot * (1 - j) * sc2)
        drawLine(-x2 / 2, 0f, x2 / 2 + x1, 0f, paint)
        restore()
    }
    restore()
}

fun Canvas.drawTTDNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawTableTopDown(scale, w, h, paint)
}

class TableTopDownView(ctx : Context) : View(ctx) {

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

        data class TTDNode(var i : Int, val state : State = State()) {

            private var next : TTDNode? = null
            private var prev : TTDNode? = null

            init {
                addNeighbor()
            }

            fun addNeighbor() {
                if (i < colors.size - 1) {
                    next = TTDNode(i + 1)
                    next?.prev = this
                }
            }

            fun draw(canvas : Canvas, paint : Paint) {
                canvas.drawTTDNode(i, state.scale, paint)
            }

            fun update(cb : (Float) -> Unit) {
                state.update(cb)
            }

            fun startUpdating(cb : () -> Unit){
                state.startUpdating(cb)
            }

            fun getNext(dir : Int, cb : () -> Unit) : TTDNode {
                var curr : TTDNode? = prev
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