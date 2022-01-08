package com.example.concentriccirclesidewiseview

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
val circles : Int = 3
val parts : Int = circles + 2
val delay : Long = 20
val deg : Float = 45f
val sizeFactor : Float = 4.9f
val strokeFactor : Float = 90f
val bacKColor : Int = Color.parseColor("#BDBDBD")
val divideFactor : Float = 1.4f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawConcentricCircleSideWise(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(circles, parts)
    val sc2 : Float = scale.divideScale(circles + 1, parts)
    save()
    translate(w / 2 , h / 2 + (h / 2 + size * circles) * sc2)
    rotate(deg * sc2 )
    for (k in 0..1) {
        save()
        rotate(deg * (1f - 2 * k) * sc1)
        for (j in 0..1) {
            var x : Float = 0f
            var barSize : Float = size
            save()
            scale(1f - 2 * j, 1f)
            for (i in 0..(circles - 1)) {
                save()
                translate(x, 0f)
                drawArc(
                    RectF(-barSize / 2, -barSize / 2, barSize / 2, barSize / 2),
                    0f,
                    360f * scale.divideScale(i, parts),
                    false,
                    paint
                )
                restore()
                x += (barSize + barSize / divideFactor)
                barSize /= divideFactor
            }
            restore()
        }
        restore()
    }
    restore()
}

fun Canvas.drawCCSWNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.style = Paint.Style.STROKE
    drawConcentricCircleSideWise(scale, w, h, paint)
}

class ConcentricCircleSideWiseView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}