package com.example.linkedlineboxesview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Canvas
import android.graphics.RectF

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
val lines : Int = 2
val boxes : Int = lines + 1
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.8f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val boxSizeFactor : Float = 11.2f
val deg : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawLinkedLineBoxes(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val boxSize : Float = Math.min(w, h) / boxSizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    save()
    translate(w / 2, h / 2 + (h / 2 + size + boxSize) * sc4)
    rotate(deg * sc3)
    for (j in 0..(lines - 1)) {
        val sc2j : Float = sc2.divideScale(j, parts)
        save()
        translate(-size + j * size, 0f)
        if (sc1 > 0) {
            drawLine(0f, 0f, size * sc2j, 0f, paint)
        }
        restore()
    }
    for (j in 0..(boxes - 1)) {
        val sc1j : Float = sc1.divideScale(j, parts)
        val upBoxSize : Float = boxSize * sc1j
        save()
        translate(-size + j * size, 0f)
        drawRect(RectF(-upBoxSize / 2, -upBoxSize / 2, upBoxSize / 2, upBoxSize / 2), paint)
        restore()
    }
    restore()
}

fun Canvas.drawLLBNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawLinkedLineBoxes(scale, w, h, paint)
}
