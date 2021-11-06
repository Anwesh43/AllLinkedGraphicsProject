package com.example.quarterarcfillview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.graphics.Path

val colors : Array<Int> = arrayOf(
    "#01579B",
    "#D50000",
    "#DD2C00",
    "#2962FF",
    "#00838F"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 4
val scGap : Float = 0.03f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val backColor : Int = Color.parseColor("#BDBDBD")
val delay : Long = 20
val deg : Float = 300f
val start : Float = 60f
val rot : Float = 180f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawQuarterFillPath(sc1 : Float, sc2 : Float, size : Float, paint : Paint) {
    save()
    val path : Path = Path()
    path.arcTo(RectF(-size / 2, -size / 2, size / 2, size / 2), -start, deg * sc1, false)
    paint.style = Paint.Style.STROKE
    drawPath(path, paint)
    clipPath(path)
    paint.style = Paint.Style.FILL
    drawRect(RectF(-size / 2, size / 2 - size * sc2, size / 2, size / 2), paint)
    restore()
}

fun Canvas.drawQuarterArcFill(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    save()
    translate(w / 2, h / 2 + (h / 2 + size) * sc4)
    rotate(rot * sc3)
    drawQuarterFillPath(sc1, sc2, size, paint)
    restore()
}

fun Canvas.drawQAFNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawQuarterArcFill(scale, w, h, paint)
}
