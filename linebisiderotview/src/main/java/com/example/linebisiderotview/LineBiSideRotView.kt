package com.example.linebisiderotview

import android.view.View
import android.view.MotionEvent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
val parts : Int = 5
val scGap : Float = 0.04f / parts
val deg : Float = 90f
val rot : Float = 45f
val delay : Long = 20
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val lSizeFactor : Float = 11.2f
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawLineBiSideRot(scale : Float, w : Float, h : Float, paint : Paint) {
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    val size : Float = Math.min(w, h) / sizeFactor
    val lSize : Float = Math.min(w, h) / lSizeFactor
    save()
    translate(w / 2, h / 2 + (h / 2 + size) * sc5)
    rotate(deg * sc4)
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f)
        save()
        translate(-size / 2, 0f)
        for (k in 0..1) {
            save()
            rotate(rot * sc3 * (1f - 2 * j))
            drawLine(0f, 0f, 0f, lSize * 0.5f * (1f - 2 * k) * sc2, paint)
            restore()
        }
        restore()
        restore()
    }
    drawLine(-size * 0.5f * sc1, 0f, size * 0.5f * sc1, 0f, paint)
    restore()
}

fun Canvas.drawLBSRNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawLineBiSideRot(scale, w, h, paint)
}

class LineBiSideRotView(ctx : Context) : View(ctx) {

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

