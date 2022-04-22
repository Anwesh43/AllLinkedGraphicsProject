package com.example.connectblocklineview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Color
import android.graphics.RectF
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
val parts : Int = 5
val scGap : Float = 0.04f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 2.2f
val barSizeFactor : Float = 11.9f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawConnectBlockLine(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val barSize : Float = Math.min(w, h) / barSizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    save()
    translate(w / 2, h / 2)
    rotate(90f * sc4)
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f - 2 * j)
        for (k in 0..1) {
            save()
            translate(size / 4, -size / 4)
            rotate(deg * k)
            drawLine(
                -size * 0.25f,
                -size / 4,
                -size * 0.25f + size * 0.5f * scale.divideScale(1 + k, parts),
                -size / 4,
                paint
            )
            drawRect(
                RectF(
                    -barSize * 0.5f * sc1,
                    -barSize * 0.5f * sc1,
                    barSize * 0.5f * sc1,
                    barSize * 0.5f * sc1
                ), paint)
            restore()
        }
        restore()
    }
    restore()
}

fun Canvas.drawCBLNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat() * (1 - scale.divideScale(4, parts))
    val h : Float = height.toFloat() * (1 - scale.divideScale(4, parts))
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawConnectBlockLine(scale, w, h, paint)
}

class ConnectBlockLineView(ctx : Context) : View(ctx) {

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
}