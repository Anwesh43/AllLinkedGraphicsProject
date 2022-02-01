package com.example.closeopenboxview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Canvas

val colors : Array<Int> = arrayOf(
    "#4A148C",
    "#004D40",
    "#DD2C00",
    "#D50000",
    "#43A047"
).map {
    Color.parseColor("#BDBDBD")
}.toTypedArray()
val delay : Long = 20
val strokeFactor : Float = 90f
val parts : Int = 4
val scGap : Float = 0.04f / parts
val rot : Float = 45f
val backColor : Int = Color.parseColor("#BDBDBD")
val sizeFactor : Float = 4.9f
