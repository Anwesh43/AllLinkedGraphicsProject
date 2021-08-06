package com.example.biarcjointhendivideview

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
    Color.parseColor("#BDBDBD")
}.toTypedArray()
val parts : Int = 3
val scGap : Float = 0.03f / parts
val sizeFactor : Float = 4.9f
val deg : Float = 180f
val backColor : Int = Color.parseColor("#BDBDBD")
