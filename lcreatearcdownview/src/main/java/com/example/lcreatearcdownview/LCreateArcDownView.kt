package com.example.lcreatearcdownview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Color
import android.graphics.Canvas

val colors : Array<Int> = arrayOf(
    "#01579B",
    "#D50000",
    "#DD2C00",
    "#2962FF",
    "#00838F"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 5
val scGap : Float = 0.04f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val arcSizeFactor : Float = 14.9f
val delay : Long = 20
val deg : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawLCreateArcDown(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val arcSize : Float = Math.min(w, h) / arcSizeFactor
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    val initY : (Int) -> Float = {
        ((-h / 2) * (1 - it) + (w / 2) * it)  * (1 - scale.divideScale(it, parts))
    }
    save()
    translate(w / 2, h / 2 + (h / 2) * sc5)
    rotate(deg * 1.5f * sc4)
    for (j in 0..1) {
        save()
        translate(0f, initY(j))
        rotate(deg * j)
        drawLine(0f, 0f, 0f, -size, paint)
        restore()
    }
    drawArc(
        RectF(
            -arcSize / 2,
            -arcSize / 2,
            arcSize / 2,
            arcSize / 2),
        -deg,
        deg * sc3,
        true,
        paint
    )
    restore()
}

fun Canvas.drawLCADNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawLCreateArcDown(scale, w ,h, paint)
}

class LCreateArcDownView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }
}