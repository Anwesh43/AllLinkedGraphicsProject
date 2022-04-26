package com.example.linesplitboxview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Color
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
val sizeFactor : Float = 4.9f
val boxSizeFactor : Float = 11.2f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawLineSplitBox(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val boxSize : Float = Math.min(w, h) / boxSizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    val sqSize : Float = boxSize * 0.5f * sc3
    save()
    translate(w / 2, h / 2 + (h / 2 + size / 2) * sc5)
    rotate(deg * sc4)
    drawLine(-size * sc1 * 0.5f, 0f, size * sc1 * 0.5f, 0f, paint)
    drawLine(-size / 2, -boxSize * sc2 * 0.5f, -size / 2, boxSize * sc2 * 0.5f, paint)
    save()
    translate(size / 2, 0f)
    drawRect(RectF(-sqSize, -sqSize, sqSize, sqSize), paint)
    restore()
    restore()
}

fun Canvas.drawLSBNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawLineSplitBox(scale, w, h, paint)
}