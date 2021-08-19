package com.example.squarecreatedropview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Color
import android.graphics.RectF
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
val parts : Int = 7
val scGap : Float = 0.06f / parts
val delay : Long = 20
val sizeFactor : Float = 3.2f
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 90f
val strokeFactor : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawSquareCreateDrop(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(4, parts)
    val sc2 : Float = scale.divideScale(5, parts)
    val sc3 : Float = scale.divideScale(6, parts)
    save()
    translate(w / 2, h / 2)
    for (j in 0..3) {
        save()
        rotate(-90f * j)
        translate(size / 2, size / 2)
        save()
        rotate(90f * (j / 3) * sc2)
        drawLine(0f, 0f, 0f, -size * scale.divideScale(j, parts), paint)
        restore()
        restore()
    }
    val rSize : Float = size * sc1
    save()
    translate(0f, (h / 2 + size) * sc3)
    drawRect(RectF(-rSize / 2, -rSize / 2, rSize / 2, rSize / 2), paint)
    restore()
    restore()
}

fun Canvas.drawSCDNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
}

class SquareCreateDropView(ctx : Context) : View(ctx) {

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