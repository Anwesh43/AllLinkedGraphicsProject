package com.example.trapebarjoinview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Color
import android.content.Context
import android.app.Activity

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
val scGap : Float = 0.04f / parts
val delay : Long = 20
val sizeFactor : Float = 4.9f
val strokeFactor : Float = 90f
val deg : Float = 180f
val backColor : Int = Color.parseColor("#BDBDBD")
val barWFactor : Float = 6.4f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawTrapeBarJoin(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val barW : Float = size / barWFactor
    save()
    translate(w / 2, h / 2 + (h / 2) * sc4)
    rotate(deg * sc3)
    for (j in 0..1) {
        val scj : Float = scale.divideScale(j, parts)
        val barH : Float = size * 0.5f  + size * 0.5f * j
        save()
        translate(-size / 2 + barW / 2 + size * j, 0f)
        drawRect(RectF(-barW / 2, -barH * scj, barW / 2, 0f), paint)
        restore()
    }
    drawLine(
        -size / 2 + barW / 2,
        -size / 2,
        -size / 2 + barW / 2 + size * sc3,
        size / 2 - size * 0.5f * sc3,
        paint
    )
    restore()
}

fun Canvas.drawTBJNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawTrapeBarJoin(scale, w, h, paint)
}

class TrapeBarJoinView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}