package com.example.multipletimesrotarcview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Canvas
import android.graphics.RectF
import android.content.Context

val colors : Array<String> = arrayOf(
    "#01579B",
    "#D50000",
    "#DD2C00",
    "#2962FF",
    "#00838F"
)
val parts : Int = 5
val scGap : Float = 0.04f / parts
val delay : Long = 20
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val deg : Float = 90f
val rot : Float = 90f
val sweep : Float = 180f
val backColor : Int = Color.parseColor("#BDBDBD")

