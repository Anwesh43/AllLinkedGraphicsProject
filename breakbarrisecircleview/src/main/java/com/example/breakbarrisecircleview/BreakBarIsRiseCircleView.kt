package com.example.breakbarrisecircleview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Canvas
import android.graphics.Color

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
val delay : Long = 20
val rot : Float = 90f
val sizeFactor : Float = 4.9f
val strokeFactor : Float = 90f
val deg : Float = 180f
val backColor : Int = Color.parseColor("#BDBDBD")
val barWFactor : Float = 5.9f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawBreakBarRiseCircle(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val barW : Float = size / barWFactor
    paint.style = Paint.Style.FILL
    save()
    translate(w / 2, h / 2)
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f)
        translate((w / 2) * sc4, 0f)
        rotate(rot * sc2)
        drawRect(RectF(0f, -size * sc1, barW, 0f), paint)
        restore()
    }
    paint.style = Paint.Style.STROKE
    save()
    translate(0f, -barW)
    drawArc(
        RectF(
            -size / 2,
            -size / 2,
            size / 2,
            size / 2
        ),
        deg * sc4,
        deg * (sc3 - sc4),
        false,
        paint
    )
    restore()
    restore()
}

fun Canvas.drawBBRCNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawBreakBarRiseCircle(scale, w, h, paint)
}
