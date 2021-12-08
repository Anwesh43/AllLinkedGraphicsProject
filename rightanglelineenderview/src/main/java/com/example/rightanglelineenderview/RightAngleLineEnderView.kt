package com.example.rightanglelineenderview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color

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
val strokeFactor : Float = 90f
val sizeFactor : Float = 3.8f
val deg : Float = 135f
val backColor : Int = Color.parseColor("#BDBDBD")
val lineFactor : Float = 4.2f
val parts : Int = 4
val rot : Float = -90f
val scGap : Float = 0.04f / parts

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawRightAngleLineEnder(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val lSize : Float = size / lineFactor
    val ulSize : Float = lSize * 0.5f * sc2
    save()
    translate(w / 2, h / 2)
    rotate(deg * sc3)
    for (j in 0..1) {
        save()
        rotate(rot * j)
        drawLine(0f, 0f, size * sc1, 0f, paint)
        drawLine(size, -ulSize, size, ulSize, paint)
        restore()
    }
    restore()
}

fun Canvas.drawRALENode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawRightAngleLineEnder(scale, w, h, paint)
}
