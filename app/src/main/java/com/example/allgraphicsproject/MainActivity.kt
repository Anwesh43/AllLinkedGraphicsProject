package com.example.allgraphicsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
//import com.example.linecontinuepathsquareview.LineContinuePathSquareView

//import com.example.linedroptosquarefallview.LineDropToSquareFallView

//import com.example.horizlinetosquaredropview.HorizLineToSquareDropView

//import com.example.squarecreatedropview.SquareCreateDropView

//import com.example.bihalfarcsideview.BiHalfArcSideView

//import com.example.piestrokethenfillview.PieStrokeThenFillView

//import com.example.arcfilldroptosquareview.ArcFillDropToSquareView

//import com.example.bifillcirctolineview.BiFillCircToLineView

//import com.example.crosstouparrowview.CrossToUpArrowView
//import com.example.rotswitchtosquareview.RotSwitchToSquareView

//import com.example.linesquarediveview.LineSquareDiveView
//import com.example.squarebreakthenmoveview.SquareBreakThenMoveView

//import com.example.trappathmoveview.TrapPathMoveView

//import com.example.tripathtosquareview.TriPathToSquareView

//import com.example.biarcjointhendivideview.BiArcJoinThenDivideView

//import com.example.biarcjoinmoveview.BiArcJoinMoveView

//import com.example.lineslidesweepboxview.LineSlideSweepBoxView

//import com.example.bisquaremergesquareview.BiSquareMergeSquareView

//import com.example.barslideupvanishview.BarSlideUpVanishView
//import com.example.squaresrotpushlineview.SquaresRotPushLineView

//import com.example.rightanglesquareleftview.RightAngleSquareLeftView

//import com.example.ballfromdiagdownupview.BallFromDiagDownUpView

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
        //BallFromDiagDownUpView.create(this)
        //RightAngleSquareLeftView.create(this)
        //BarSlideUpVanishView.create(this)
        //BiSquareMergeSquareView.create(this)
        //LineSlideSweepBoxView.create(this)
        // BiArcJoinMoveView.create(this)
        //BiArcJoinThenDivideView.create(this)
        //TriPathToSquareView.create(this)
        //TrapPathMoveView.create(this)
        //LineSquareDiveView.create(this)
        //SquareBreakThenMoveView.create(this)
        //CrossToUpArrowView.create(this)
        //BiFillCircToLineView.create(this)
        //ArcFillDropToSquareView.create(this)
        //PieStrokeThenFillView.create(this)
        //BiHalfArcSideView.create(this)
        //SquareCreateDropView.create(this)
        //HorizLineToSquareDropView.create(this)
        //LineDropToSquareFallView.create(this)
        //LineContinuePathSquareView.create(this)
        fullScreen()
    }
}

fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}