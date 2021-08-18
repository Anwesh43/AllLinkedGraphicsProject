package com.example.bihalfarcsideview

import android.view.View
import android.view.MotionEvent
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Paint
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
val parts : Int = 4
val scGap : Float = 0.04f / parts
val sizeFactor : Float = 4.9f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 180f


fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawBiHalfArcSide(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    save()
    translate(w / 2, h / 2 + (h / 2 + size) * sc4)
    for (j in 0..1) {
        save()
        translate(size * 0.5f * (1 - 2 * j) * (1 - sc3), 0f)
        drawArc(
            RectF(-size / 2, -size / 2, size / 2, size / 2),
            180f * j, 180f * scale.divideScale(j, parts),
            true,
            paint
        )
        restore()
    }
    restore()
}

fun Canvas.drawBHASNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    drawBiHalfArcSide(scale, w, h, paint)
}
