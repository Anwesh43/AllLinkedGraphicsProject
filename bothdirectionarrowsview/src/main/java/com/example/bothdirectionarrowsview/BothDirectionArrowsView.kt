package com.example.bothdirectionarrowsview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.graphics.Paint
import android.graphics.Canvas
import android.content.Context
import android.graphics.Color

val color : Array<Int> = arrayOf(
    "#1A237E",
    "#00C853",
    "#AA00FF",
    "#01579B",
    "#BF360C"
).map {
    Color.parseColor(it)
}.toTypedArray()
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val delay : Long = 20
val parts : Int = 3
val backColor : Int = Color.parseColor("#BDBDBD")
val scGap : Float = 0.03f / parts
val heightFactor : Float = 23.2f

