package com.example.peaksquareholderview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.RectF
import android.app.Activity
import android.graphics.Color
import android.graphics.Canvas
import android.content.Context

val colors : Array<Int> = arrayOf(
    "#E53935",
    "#00C853",
    "#0D47A1",
    "#AA00FF",
    "#FF3D00"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 4
val scGap : Float = 0.04f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val delay : Long = 20
val deg : Float = 90f
val backColor : Int = Color.parseColor("#BDBDBD")
