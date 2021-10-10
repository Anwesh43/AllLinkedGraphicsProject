package com.example.downloadiconmoveview

import android.view.View
import android.view.MotionEvent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Canvas
import android.app.Activity
import android.content.Context

val colors : Array<Int> = arrayOf(
    "#D32F2F",
    "#006064",
    "#01579B",
    "#BF360C",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 5
val scGap : Float = 0.04f / parts
val strokeFactor : Float = 90f
val deg : Float = 45f
val rot : Float = 90f
val sizeFactor : Float = 4.9f
val arrowSizeFactor : Float = 5.2f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")

