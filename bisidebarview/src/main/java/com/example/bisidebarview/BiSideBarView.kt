package com.example.bisidebarview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.app.Activity
import android.content.Context

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#00C853",
    "#AA00FF",
    "#01579B",
    "#BF360C"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 4
val scGap : Float = 0.04f / parts
val rot : Float = -90f
val backColor : Int = Color.parseColor("#BDBDBD")
val delay : Long = 20
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val barWFactor : Float = 13.2f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawBiSideBar(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val sqSize : Float = size / barWFactor
    save()
    translate(w / 2, h / 2 + (h / 2 + paint.strokeWidth) * sc4)
    rotate(rot * sc3)
    drawLine(0f,-size * 0.5f * sc1, 0f, size * 0.5f * sc1, paint)
    for (j in 0..1) {
        save()
        scale(1f, 1f - 2 * j)
        translate(0f, -size / 2)
        drawRect(RectF(-size * sc2, 0f, 0f, sqSize), paint)
        restore()
    }
    restore()
}

fun Canvas.drawBSBNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    drawBiSideBar(scale, w, h, paint)
}
