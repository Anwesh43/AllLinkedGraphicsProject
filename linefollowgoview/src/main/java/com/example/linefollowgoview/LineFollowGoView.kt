package com.example.linefollowgoview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.graphics.Paint
import android.content.Context
import android.graphics.Color
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
val delay : Long = 20
val lines : Int = 3
val parts : Int = 2
val scGap : Float = 0.06f / (parts * lines)
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.2f
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawLineFollowGo(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    save()
    translate(w / 2, h / 2)
    for (j in 0..(lines - 1)) {
        val sc1j : Float = sc1.divideScale(j, lines)
        val sc2j : Float = sc2.divideScale(j, lines)
        save()
        translate(0f, h / 2)
        rotate(deg * sc1j * j)
        drawLine(-size / 2, -size * sc2j, -size / 2, -size * sc1j, paint)
        restore()
    }
    restore()
}

fun Canvas.drawLFGNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawLineFollowGo(scale, w, h, paint)
}
