package com.example.allgraphicsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.ballfromdiagdownupview.BallFromDiagDownUpView

//import com.example.blockrotatedownview.BlockRotateDownView

//import com.example.pauserotlineview.PauseRotLineView

//import com.example.bilinearcwheelview.BiLineArcWheelView

//import com.example.pietotmoveview.PieToTMoveView

//import com.example.rectchairlineview.RectChairLineView

//import com.example.reactlogoellipview.ReactLogoEllipView

//import com.example.concarclinejoinerview.ConcArcLineJoinerView
//import com.example.createsteptomoveview.CreateStepToMoveView
//import com.example.rotatesemiarcmoveview.RotateSemiArcMoveView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         //RotateSemiArcMoveView.create(this)
        //ReactLogoEllipView.create(this)
        //RectChairLineView.create(this)
        //PieToTMoveView.create(this)
        //BiLineArcWheelView.create(this)
        //PauseRotLineView.create(this)
        //BlockRotateDownView.create(this)
        BallFromDiagDownUpView.create(this)
        fullScreen()
    }
}

fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}