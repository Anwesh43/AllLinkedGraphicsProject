package com.example.direclinescreateview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
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
val parts : Int = 4
val scGap : Float = 0.03f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 5.2f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawEndStartLine(scale : Float, i : Int, gap : Int, x : Float, y : Float, paint : Paint) {
    val scStart : Float = scale.divideScale(i, parts)
    val scEnd : Float = scale.divideScale(i + gap, parts)
    drawLine(x * scEnd, y * scEnd, x * scStart, y * scEnd, paint)
}

fun Canvas.drawDirecLinesCreate(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    save()
    translate(w / 2, h / 2)
    save()
    translate(0f, -h / 2)
    drawEndStartLine(scale, 1, 2, 0f, h, paint)
    restore()
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f)
        translate(w / 2 - size, h / 2)
        drawEndStartLine(scale, 0, 2, size, -size, paint)
        restore()
    }
    restore()
}

fun Canvas.drawDLCNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawDirecLinesCreate(scale, w, h, paint)
}
