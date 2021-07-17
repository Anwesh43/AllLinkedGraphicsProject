package com.example.allgraphicsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.concarclinejoinerview.ConcArcLineJoinerView
import com.example.createsteptomoveview.CreateStepToMoveView
import com.example.rotatesemiarcmoveview.RotateSemiArcMoveView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         RotateSemiArcMoveView.create(this)
        fullScreen()
    }
}

fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}