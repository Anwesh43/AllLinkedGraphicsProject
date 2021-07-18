package com.example.reactlogoellipview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.RectF
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
val parts : Int = 4
val scGap : Float = 0.02f / parts
val rot : Float = 45f
val delay : Long = 20
val sizeFactor : Float = 3.2f
val sizeHFactor : Float = 7.8f
val rFactor : Float = 12.8f
val backColor : Int = Color.parseColor("#BDBDBD")
