package com.example.bararcextenderview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Color
import android.app.Activity
import android.content.Context

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
val scGap : Float = 0.04f / parts
val arcFactor : Float = 5.9f
val barHFactor : Float = 13.2f
val delay : Long = 20
val deg : Float = 90f
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawBarArcExtender(scale : Float, w : Float, h : Float, paint : Paint) {
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val arcR : Float = Math.min(w, h) / arcFactor
    val barH : Float = Math.min(w, h) / barHFactor
    save()
    translate(w / 2, h / 2)
    save()
    translate(0f, h /2 - h * sc4)
    drawArc(
        RectF(-arcR / 2, -arcR / 2, arcR / 2, arcR / 2),
        180f,
        180f * sc2,
        true,
        paint
    )
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f)
        drawRect(RectF(-w / 2 + w * 0.5f * sc3, -barH, -w / 2 + w * 0.5f * sc1, 0f), paint)
        restore()
    }
    restore()
    restore()
}

fun Canvas.drawBAENode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    drawBarArcExtender(scale, w, h, paint)
}