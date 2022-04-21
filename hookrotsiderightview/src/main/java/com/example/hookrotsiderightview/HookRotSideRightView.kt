package com.example.hookrotsiderightview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
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
val parts : Int = 5
val scGap : Float = 0.05f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val delay : Long = 20
val rot : Float = 90f
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int): Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawHookSideRight(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    save()
    translate(w / 2 + (w / 2) * sc5, h / 2)
    rotate(rot * sc4)
    if (sc1 > 0f) {
        drawLine(0f, 0f, 0f, -size * sc1, paint)
    }
    if (sc2 > 0f) {
        drawLine(0f, -size, size * 0.5f * sc2, -size, paint)
    }
    if (sc3 > 0f) {
        drawLine(size / 2, -size, size / 2, -size + size * 0.5f * sc3, paint)
    }
    restore()
}

fun Canvas.drawHRSRNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    drawHookSideRight(scale, w, h, paint)
}

class HookRotSideRightView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}