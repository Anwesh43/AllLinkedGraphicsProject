package com.example.divideballupview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
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
val balls : Int = 3
val parts : Int = balls + 2
val sizeFactor : Float = 2.8f
val ballDivideFactor : Float = 2.3f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 180f
val scGap : Float = 0.04f / parts

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawDivideBallUp(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(3, parts)
    val sc2 : Float = scale.divideScale(4, parts)
    save()
    translate(w / 2, h / 2 + (h / 2 + size) * sc2)
    rotate(deg * sc1)
    var y : Float = 0f
    var k : Float = 1f
    for (j in 0..(balls - 1)) {
        val r : Float = size / (2 * k)
        save()
        translate(0f, y)
        drawArc(
            RectF(-r, -r, r, r),
            0f,
            deg * 2 * scale.divideScale(j, parts),
            true,
            paint
        )
        restore()
        k *= ballDivideFactor
        y -= (r + r / ballDivideFactor)
    }
    restore()
}

fun Canvas.drawDBUNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    drawDivideBallUp(scale, w, h, paint)
}
