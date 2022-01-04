package com.example.enclosedaltsidebarview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Color
import android.graphics.RectF
import android.graphics.Canvas

val colors : Array<Int> = arrayOf(
    "#01579B",
    "#D50000",
    "#DD2C00",
    "#2962FF",
    "#00838F"
).map {
    Color.parseColor(it)
}.toTypedArray()
val delay : Long = 20
val sizeFactor : Float = 5.9f
val strokeFactor : Float = 90f
val parts : Int = 6
val scGap : Float = 0.04f / parts
val deg : Float = 90f
val backColor : Int = Color.parseColor("#BDBDBD")
