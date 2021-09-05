package com.example.trilinecreateendview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Canvas

val colors : Array<Int> = arrayOf(
    "#304FFE",
    "#C51162",
    "#F57F17",
    "#00C853",
    "#FFD600"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 6
val scGap : Float = 0.06f / parts
val strokeFactor : Float = 90f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
