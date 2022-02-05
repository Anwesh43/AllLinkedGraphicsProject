package com.example.uprectwingsview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.graphics.Color
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Paint
import android.content.Context

val colors : Array<Int> = arrayOf(
    "#F44336",
    "#2196F3",
    "#FF9800",
    "#795548",
    "#8BC34A"
).map {
    Color.parseColor(it)
}.toTypedArray()
val sizeFactor : Float = 4.9f
val strokeFactor : Float = 90f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 180f
val parts : Int = 4
val scGap : Float = 0.04f / parts

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
