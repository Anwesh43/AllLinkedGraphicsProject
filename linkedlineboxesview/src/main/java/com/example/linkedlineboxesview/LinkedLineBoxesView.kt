package com.example.linkedlineboxesview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Canvas
import android.graphics.RectF

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#00C853",
    "#AA00FF",
    "#01579B",
    "#BF360C"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 3
val scGap : Float = 0.03f / parts
val lines : Int = 2
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.8f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
