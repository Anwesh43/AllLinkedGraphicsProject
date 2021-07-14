package com.example.linefollowgoview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.graphics.Paint
import android.content.Context
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
val delay : Long = 20
val lines : Int = 3
val parts : Int = 2
val scGap : Float = 0.06f / (parts * lines)
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.2f
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
