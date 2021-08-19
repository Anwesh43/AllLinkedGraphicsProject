package com.example.squarecreatedropview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Color
import android.graphics.RectF
import android.graphics.Canvas
import android.app.Activity
import android.content.Context

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#EF5350",
    "#AA00FF",
    "#C51162",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 6
val scGap : Float = 0.06f / parts
val delay : Long = 20
val sizeFactor : Float = 3.2f
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 90f
val strokeFactor : Float = 90f
