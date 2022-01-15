package com.example.boxuptodownonlineview

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
val parts : Int = 5
val scGap : Float = 0.04f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val rot : Float = 180f
val boxSizeFactor : Float = 4.9f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawBoxUpToDownOnline(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val boxSize : Float = size / boxSizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    val upSize : Float = size * 0.5f * (sc1 - sc5)
    val y : Float = -h / 2 + h / 2 * (sc2 - sc4)
    save()
    translate(w / 2, h / 2)
    rotate(rot * sc3)
    drawLine(-upSize, 0f, upSize, 0f, paint)
    save()
    translate(0f, y)
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f)
        translate(-size / 2, 0f)
        drawRect(RectF(0f, -boxSize, boxSize, 0f), paint)
        restore()
    }
    restore()
    restore()
}

fun Canvas.drawBUTDONode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawBoxUpToDownOnline(scale, w, h, paint)
}

