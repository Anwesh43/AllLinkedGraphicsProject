package com.example.arcgotooppositelineview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Color
import android.graphics.Canvas
import android.content.Context
import android.app.Activity

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
val sizeFactor : Float = 6.9f
val strokeFactor : Float = 90f
val delay : Long = 20
val scGap : Float = 0.04f / parts
val backColor : Int = Color.parseColor("#BDBDBD")
val deg : Float = 360f
