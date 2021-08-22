package com.example.linecontinuepathsquareview

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
    "#EF5350",
    "#AA00FF",
    "#C51162",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val delay : Long = 20
val lines : Int = 3
val parts : Int =  lines + 2
val scGap : Float = 0.05f / parts
val deg : Float = 90f
val backColor : Int = Color.parseColor("#BDBDBD")
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.5f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawLineContinuePathSquare(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    save()
    translate(w / 2, h / 2 - (h / 2 + size / 2) * sc5)
    for (j in 0..(lines - 1)) {
        val scj : Float = scale.divideScale(j, parts)
        save()
        rotate(deg * j)
        drawLine(
            size / 2,
            size / 2,
            size / 2,
            size / 2 - size * scj,
            paint
        )
        restore()
    }
    save()
    translate(0f, -(h / 2 + size / 2) * (1 - sc4))
    drawRect(RectF(-size / 2, -size / 2, size / 2, size / 2), paint)
    restore()
    restore()
}

fun Canvas.drawLCPSNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawLineContinuePathSquare(scale, w, h, paint)
}
