package com.example.arrowsignshootview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.Paint

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
val delay : Long = 20
val deg : Float = 45f
val backColor : Int = Color.parseColor("#BDBDBD")
val strokeFactor : Float = 90f
val sizeFactor : Float= 5.9f
val triSizeFactor : Float = 12.9f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawTriangle(x : Float, y : Float, size : Float, paint : Paint) {
    save()
    translate(x, y)
    val path : Path = Path()
    path.moveTo(-size / 2, 0f)
    path.lineTo(0f, -size)
    path.lineTo(size / 2, 0f)
    path.lineTo(-size / 2, 0f)
    drawPath(path, paint)
    restore()
}

fun Canvas.drawArrowSignShoot(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val triSize : Float = Math.min(w, h) / triSizeFactor
    save()
    translate(w / 2, h / 2 - (h / 2) * sc4)
    rotate(deg * sc3)
    drawLine(0f, 0f, 0f, -size * sc1, paint)
    drawTriangle(0f, -h / 2 + (h / 2 - size) * sc2, triSize, paint)
    restore()
}

fun Canvas.drawASSVNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawArrowSignShoot(scale, w, h, paint)
}

class ArrowSignShootView(ctx : Context) : View(ctx) {

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