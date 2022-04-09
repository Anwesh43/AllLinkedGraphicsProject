package com.example.blocklinearrocreatorview

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
val parts : Int = 5
val scGap : Float = 0.04f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val blockSizeFactor : Float = 9.9f
val lineSizeFactor : Float = 7.9f
val delay : Long = 20
val deg : Float = 90f
val rot : Float = 45f
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawBlockLineArrowCreator(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    val lineSize : Float = size / lineSizeFactor
    val blockSize : Float = size / blockSizeFactor
    save()
    translate(w / 2, h / 2 + (h / 2 + size + lineSize) * sc5)
    rotate(rot * sc4)
    drawLine(-size * 0.5f * sc1, 0f, size * 0.5f * sc1, 0f, paint)
    for (j in 0..1) {
        save()
        translate(-size / 2, 0f)
        scale(1f, 1f - 2 * j)
        rotate(deg * sc3)
        drawLine(0f, 0f, 0f, -lineSize * sc2, paint)
        restore()
    }
    save()
    translate(size / 2, 0f)
    drawRect(RectF(0f, -blockSize / 2, blockSize * sc1, blockSize / 2), paint)
    restore()
    restore()
}

fun Canvas.drawBLACNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawBlockLineArrowCreator(scale, w, h, paint)
}
