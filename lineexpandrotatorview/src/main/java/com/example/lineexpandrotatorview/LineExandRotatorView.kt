package com.example.lineexpandrotatorview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.app.Activity
import android.content.Context

val colors : Array<Int> = arrayOf(
    "#2962FF",
    "#BF360C",
    "#00C853",
    "#C51162",
    "#FFD600"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 4
val scGap : Float = 0.02f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val rot : Float = 180f
val sizeFactor : Float = 4.9f
val strokeFactor : Float = 90f