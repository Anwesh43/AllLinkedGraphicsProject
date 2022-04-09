package com.example.blocklinearrocreatorview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Canvas
import android.graphics.Color

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#00C853",
    "#AA00FF",
    "#01579B",
    "#BF360C"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 5
val scGap : Float = 0.04f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 4.9f
val blockSizeFactor : Float = 9.9f
val lineSizeFactor : Float = 7.9f
val delay : Long = 20
val deg : Float = 90f
val rot : Float = 45f
val backColor : Int = Color.parseColor("#BDBDBD")