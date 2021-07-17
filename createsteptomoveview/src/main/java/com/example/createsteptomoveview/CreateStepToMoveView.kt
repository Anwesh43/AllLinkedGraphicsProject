package com.example.createsteptomoveview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Canvas

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#EF5350",
    "#AA00FF",
    "#C51162",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val lines : Int = 3
val parts : Int = lines + 4
val scGap : Float = 0.07f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val rot : Float = 180f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawCreateStepToMove(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    val sc6 : Float = scale.divideScale(5, parts)
    val sc7 : Float = scale.divideScale(6, parts)
    val xGap : Float = size / lines
    save()
    translate(w / 2 + (w / 2 + size / 2) * sc7, h / 2 + (h / 2 - size / 2) * (1 - sc6))
    rotate(rot * sc7)
    for (j in 0..(lines - 1)) {
        val scj : Float = scale.divideScale(j, parts)
        val scj1 : Float = scj.divideScale(0, 2)
        val scj2 : Float = scj.divideScale(1, 2)
        save()
        translate(-size / 2 + xGap * j, size / 2 - xGap * j)
        drawLine(0f, 0f, 0f, -xGap * scj1, paint)
        drawLine(0f, -xGap, xGap * scj2, -xGap, paint)
        restore()
    }
    restore()
}

fun Canvas.drawCSTMNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawCreateStepToMove(scale, w, h, paint)
}

class CreateStepToMoveView(ctx : Context) : View(ctx) {

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