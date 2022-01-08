package com.example.concentriccirclesidewiseview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#EF5350",
    "#AA00FF",
    "#C51162",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val circles : Int = 3
val parts : Int = circles + 2
val delay : Long = 20
val deg : Float = 45f
val sizeFactor : Float = 4.9f
val strokeFactor : Float = 90f
val bacKColor : Int = Color.parseColor("#BDBDBD")
val divideFactor : Float = 1.4f
