package com.example.linedividetriballshiftview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Canvas

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#00C853",
    "#AA00FF",
    "#01579B",
    "#BF360C"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 4
val scGap : Float = 0.04f / parts
val sizeFactor : Float = 4.9f
val ballRFactor : Float = 13.2f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val strokeFactor : Float = 90f
val deg : Float = 45f
