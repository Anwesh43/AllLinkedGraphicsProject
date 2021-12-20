package com.example.wheelarcbarview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Color
import android.graphics.RectF
import android.graphics.Canvas
import android.app.Activity
import android.content.Context

val parts : Int = 3
val scGap : Float = 0.02f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 360f
val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#EF5350",
    "#AA00FF",
    "#C51162",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val rFactor : Float = 21.2f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i  : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n.inverse()

fun Canvas.drawWheelArcBar(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val r : Float = Math.min(w, h) / rFactor
    save()
    translate(w / 2 + (w / 2 + size / 2) * sc3, h / 2)
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f)
        translate(-size + r, r)
        drawArc(RectF(-r, -r, r, r), 0f, 360f * sc1, false, paint)
        restore()
    }
    save()
    translate((-w / 2 - size / 2) * (1 - sc2), 0f)
    drawRect(RectF(-size / 2, -size, size / 2, 0f), paint)
    restore()
    restore()
}

fun Canvas.drawWABNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawWheelArcBar(scale, w, h, paint)
}