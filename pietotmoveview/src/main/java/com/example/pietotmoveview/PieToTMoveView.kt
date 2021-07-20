package com.example.pietotmoveview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
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
val scGap : Float = 0.02f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.2f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val rot : Float = 45f
val deg : Float = 180f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawPieToMove(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    save()
    translate(w / 2,  h / 2 + (h / 2) * (1 - sc2))
    rotate(deg * sc2)
    for (j in 0..1) {
        save()
        translate(0f, -(h / 2) * sc4)
        rotate(deg * (1 - 2 * j) * (1 - sc3))
        drawLine(0f, 0f, 0f, -size * sc1, paint)
        restore()
    }
    save()
    translate(0f, (h / 2 + paint.strokeWidth) * sc4)
    drawLine(-size * 0.5f * sc1, 0f, size * 0.5f * sc1, 0f, paint)
    restore()
    restore()
}

fun Canvas.drawPTTMNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) /strokeFactor
    drawPieToMove(scale, w, h, paint)
}