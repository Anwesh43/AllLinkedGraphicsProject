package com.example.barbelowlineview

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
val parts : Int = 6
val scGap : Float = 0.04f / parts
val delay : Long = 20
val sizeFactor : Float = 4.9f
val strokeFactor : Float =  90f
val rot : Float = 90f
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawBarBelowLine(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc5 : Float = scale.divideScale(4, parts)
    val sc6 : Float = scale.divideScale(5, parts)
    val barSize : Float = size / 4
    save()
    translate(w / 2, h / 2 + (h / 2 + size) * sc6)
    rotate(rot * sc5)
    for (j in 0..1) {
        val sc1 : Float = scale.divideScale(j * 2, parts)
        val sc2 : Float = scale.divideScale(j * 2 + 1, parts)
        save()
        scale(1f - 2 * j, 1f)
        translate(-size / 2, 0f)
        drawRect(RectF(0f, -barSize * sc1, barSize, 0f), paint)
        drawLine(barSize / 2 - (barSize / 2) * sc2, -size / 2, barSize / 2 + (barSize / 2) * sc2, -size / 2, paint)
        restore()
    }
    restore()
}

fun Canvas.drawBBLNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawBarBelowLine(scale, w, h, paint)
}
