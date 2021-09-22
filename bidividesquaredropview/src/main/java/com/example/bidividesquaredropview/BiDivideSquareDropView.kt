package com.example.bidividesquaredropview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
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
val parts : Int = 3
val sizeFactor : Float = 5.9f
val scGap : Float = 0.03f / parts
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
