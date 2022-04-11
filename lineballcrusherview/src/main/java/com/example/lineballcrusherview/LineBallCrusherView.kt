package com.example.lineballcrusherview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Color
import android.graphics.Canvas
import android.content.Context

val parts : Int = 5
val scGap : Float = 0.04f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val rFactor : Float = 5.8f
val delay : Long = 20
val deg : Float = 90f
val colors : Array<Int> = arrayOf(
    ""
).map {
    Color.parseColor(it)
}.toTypedArray()
