package com.example.concarclinejoinerview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Color

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#EF5350",
    "#AA00FF",
    "#C51162",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 4
val concs : Int = 2
val strokeFactor : Float = 0.02f / (parts * concs)
val delay : Long = 20
val r1Factor : Float = 5.2f
val r2Factor : Float = 3.2f
val backColor : Int = Color.parseColor("#BDBDBD")

