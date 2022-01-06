package com.example.divideballupview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.app.Activity
import android.content.Context

val colors : Array<Int> = arrayOf(
    "#01579B",
    "#D50000",
    "#DD2C00",
    "#2962FF",
    "#00838F"
).map {
    Color.parseColor(it)
}.toTypedArray()
val balls : Int = 3
val parts : Int = balls + 2
val sizeFactor : Float = 2.8f
val ballDivideFactor : Float = 2.3f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 180f
