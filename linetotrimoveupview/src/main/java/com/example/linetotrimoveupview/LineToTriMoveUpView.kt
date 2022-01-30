package com.example.linetotrimoveupview

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Canvas
import android.content.Context
import android.app.Activity
import android.view.View
import android.view.MotionEvent

val colors : Array<Int> = arrayOf(
    "#E53935",
    "#00C853",
    "#0D47A1",
    "#AA00FF",
    "#FF3D00"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 4
val scGap : Float = 0.04f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val rot : Float = 45f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
