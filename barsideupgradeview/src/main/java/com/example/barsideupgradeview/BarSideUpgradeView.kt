package com.example.barsideupgradeview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.RectF
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
val bars : Int = 3
val delay : Long = 20
val parts : Int = bars + 2
val scGap : Float = 0.04f / parts
val backColor : Int = Color.parseColor("#BDBDBD")
val sizeFactor : Float = 2.3f
val divideFactor : Float = 2.1f
val rot : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawBarSideUpgrade(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(bars, parts)
    val sc2 : Float = scale.divideScale(bars + 1, parts)
    var x : Float = 0f
    save()
    translate(w / 2 + (w / 2) * sc2, h / 2)
    rotate(rot * sc1)
    var barSize : Float = size
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f)
        for (k in 0..(bars - 1)) {
            save()
            drawRect(
                RectF(
                    0f,
                    -barSize,
                    barSize * scale.divideScale(j, parts),
                    0f),
                paint)
            restore()
        }
        restore()
        x += barSize
        barSize /= divideFactor
    }
    restore()
}

fun Canvas.drawBSUNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    drawBarSideUpgrade(scale, w, h, paint)
}

class BarSideUpgradeView(ctx : Context) : View(ctx) {

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
    }
}