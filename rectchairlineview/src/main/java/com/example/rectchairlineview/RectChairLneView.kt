package com.example.rectchairlineview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.RectF
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
val strokeFactor : Float = 90f
val rhFactor : Float = 11.2f
val rwFactor : Float = 4.3f
val delay : Long = 20
val rot : Float = 180f
val backColor : Int = Color.parseColor("#BDBDBD")
val parts : Int = 4
val scGap : Float = 0.02f / parts

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawRectChairLine(scale : Float, w : Float, h : Float, paint : Paint) {
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val rw : Float = Math.min(w, h) / rwFactor
    val rh : Float = Math.min(w, h) / rhFactor
    save()
    translate(w / 2, h / 2 + (h / 2 - (rw + rh) / 2) * (1 - sc3) - (h / 2 + (rw + rh)) * sc4)
    rotate(rot * sc3)
    save()
    translate(-rw / 2, (rw + rh) / 2)
    drawRect(RectF(0f, -(rh) * sc1, rw, 0f), paint)
    for (j in 0..1) {
        save()
        translate(rw * j, 0f)
        drawLine(0f, 0f, 0f, -rh * sc2, paint)
        restore()
    }
    restore()
    restore()
}

fun Canvas.drawRCLNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawRectChairLine(scale, w, h, paint)
}
