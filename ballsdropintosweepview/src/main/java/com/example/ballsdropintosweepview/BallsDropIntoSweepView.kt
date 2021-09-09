package com.example.ballsdropintosweepview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Color
import android.graphics.Canvas
import android.app.Activity
import android.content.Context

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#EF5350",
    "#AA00FF",
    "#C51162",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val sizeFactor : Float = 5.3f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val balls : Int = 3
val parts : Int = balls + 2
val scGap : Float = 0.05f / parts
