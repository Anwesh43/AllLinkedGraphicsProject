package com.example.linetomultisquaredownview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.app.Activity
import android.content.Context

val colors : Array<Int> = arrayOf(
    "#D32F2F",
    "#006064",
    "#01579B",
    "#BF360C",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val lines : Int = 5
val parts : Int = 2
val scGap : Float = 0.15f / ((parts * lines) + 1)
val sizeFactor : Float = 4.9f
val strokeFactor : Float = 90f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawLineToMultiSquareDown(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    save()
    translate(0f, h * sc3)
    for (j in 0..(lines - 1)) {
        val sc1j : Float = sc1.divideScale(j, lines)
        val sc2j : Float = sc2.divideScale(j, lines)
        val upSize : Float = size * 0.5f * sc2j
        save()
        translate(0f, size * j + size / 2)
        drawLine(w * 0.5f * sc2j, 0f, w * 0.5f * sc1j, 0f, paint)
        save()
        translate(w / 2, 0f)
        drawRect(RectF(-upSize, -upSize, upSize, upSize), paint)
        restore()
        restore()
    }
    restore()
}

fun Canvas.drawLMSDNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    drawLineToMultiSquareDown(scale, w, h, paint)
}
