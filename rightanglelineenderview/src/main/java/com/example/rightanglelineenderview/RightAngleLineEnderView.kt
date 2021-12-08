package com.example.rightanglelineenderview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color

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
val strokeFactor : Float = 90f
val sizeFactor : Float = 3.8f
val deg : Float = 135f
val backColor : Int = Color.parseColor("#BDBDBD")
val lineFactor : Float = 4.2f
val parts : Int = 4
val rot : Float = -90f
val scGap : Float = 0.04f / parts