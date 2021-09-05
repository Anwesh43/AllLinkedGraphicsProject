package com.example.trilinecreateendview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Canvas

val colors : Array<Int> = arrayOf(
    "#304FFE",
    "#C51162",
    "#F57F17",
    "#00C853",
    "#FFD600"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 6
val scGap : Float = 0.06f / parts
val strokeFactor : Float = 90f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawUpdateLine(x : Float, y : Float, sizeX : Float, sizeY : Float, sc : Float, j : Int, paint : Paint) {
    val x1 : Float = x + sizeX * sc.divideScale(3 + j, parts)
    val y1 : Float = y + sizeY * sc.divideScale(3 + j, parts)
    val x2 : Float = x + sizeX * sc.divideScale(j, parts)
    val y2 : Float = y + sizeY * sc.divideScale(j, parts)
    drawLine(x1, y1, x2, y2, paint)
}

fun Canvas.drawTriLineCreateEnd(scale : Float, w : Float, h : Float, paint : Paint) {
    save()
    translate(w / 2, h / 2)
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f - 2 * j)
        drawUpdateLine(w / 2, 0f, -w / 2, 0f, scale, 0, paint)
        drawUpdateLine(0f, 0f, 0f, -h / 2, scale, 1, paint)
        drawUpdateLine(0f, -h / 2, w / 2, h / 2, scale, 2, paint)
        restore()
    }
    restore()
}

fun Canvas.drawTLCENode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawTriLineCreateEnd(scale, w, h, paint)
}
