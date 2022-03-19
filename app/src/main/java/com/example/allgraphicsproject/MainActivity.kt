package com.example.allgraphicsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.quarterlinerotrectupview.QuarterLineRotRectUpView

//import com.example.invertedlinetorectview.InvertedLineToRectView

//import com.example.horiztovertrectcatcherview.HorizToVertRectCatcherView

//import com.example.squaresidelinerotview.SquareSideLineRotView

//import com.example.linerotrectexpanderview.LineRotRectExpanderView

//import com.example.bothdirectionarrowsview.BothDirectionArrowsView

//import com.example.linkedcirclerotatorview.LinkedCircleRotatorView

//import com.example.linkedlineboxesview.LinkedLineBoxesView

//import com.example.expandlineandhalfarcview.ExpandHalfArcLineView

//import com.example.exclaimshooterview.ExclaimShooterView

//import com.example.hammerbarhitleftview.HammerBarHitLeftView

//import com.example.altsiderotrectupview.AltSideRotRectUpView

//import com.example.sidewiserotfillrectview.SideWiseRotFillRectView

//import com.example.multipletimesrotarcview.MultipleTimesRotArcView

//import com.example.linearchookview.LineArcHookView

//import com.example.linefromendvsweepview.LineFromEndVSweepView

//import com.example.clipboxlinerotview.ClipBoxLineRotView

//import com.example.linerottosidenview.LineRotToSideNView

//import com.example.openlineboxlineview.OpenLineBoxLineView

//import com.example.diagboatlineview.DiagBoatLineView

//import com.example.ktorottview.KRotTView

//import com.example.sidearmbarrotview.SideBarArmRotView

//import com.example.openboxcrossmoverview.OpenBoxCrossMoverView

//import com.example.siderectrotlineview.SideRectRotLineView

//import com.example.linesiderotbarview.LineSideRotView

//import com.example.uprectwingsview.UpRectWingsView

//import com.example.arrowleggedmoverview.ArrowLeggedMoverView

//import com.example.barpushlineupview.BarPushLineUpView

//import com.example.closeopenboxview.CloseOpenBoxView

//import com.example.lineballmidrotview.LineBallMidRotView

//import com.example.linetotrimoveupview.LineToTriMoveUpView

//import com.example.peaksquareholderview.PeakSquareHolderView

//import com.example.squarelineextenderview.SquareExtenderLineView

//import com.example.arcbarrotdownview.ArcBarRotDownView

//import com.example.trapebarjoinview.TrapeBarJoinView

//import com.example.halfbarrotdownview.HalfBarRotDownView

//import com.example.semiarcbarholderview.SemiArcBarHolderView

//import com.example.bilinerotatevview.BiLineRotateVView

//import com.example.boxuptodownonlineview.BoxUpToDownOnlineView

//import com.example.arrowsignshootview.ArrowSignShootView

//import com.example.barhammerdivideview.BarHammerDivideView

//import com.example.tribaroverlineview.TriBarOverLineView

//import com.example.concentriccirclesidewiseview.ConcentricCircleSideWiseView

//import com.example.barsideupgradeview.BarSideUpgradeView

//import com.example.divideballupview.DivideBallUpView

//import com.example.dividebarupview.DivideBarUpView

//import com.example.enclosedaltsidebarview.EnclosedAltSideBarView

//import com.example.umbrellarclineview.UmbreallaArcLineView

//import com.example.halfarclinevaseview.HalfArcLineVaseView

//import com.example.squaretohrotview.SquareToHRotView

//import com.example.hundredonerotview.HundredOneRotView

//import com.example.arcsweepfromsideview.ArcSweepFromSideView

//import com.example.halfarcjoindownview.HalfArcJoinDownView

//import com.example.arcjoinlinedownview.ArcJoinLineDownView

//import com.example.targetarclineview.TargetArcLineView

//import com.example.parallellinetravellerview.ParallelLineTravellerView

//import com.example.arclinedirecview.ArcLineDirecView

//import com.example.altsquarefillupview.AltSquareFillUpView

//import com.example.wheelarcbarview.WheelArcBarView

//import com.example.linesplitbarview.LineSplitBarView

//import com.example.squareballcatcherview.SquareBallCatcherView

//import com.example.pluscrossrotdownview.PlusCrossRotDownView

//import com.example.arrowheadshiftview.ArrowHeadShiftView

//import com.example.boxlinestanddownview.BoxLineStandDownView

//import com.example.parallellinetovview.ParallelLineToVView

//import com.example.tlinesidedownview.TLineSideDownView

//import com.example.joinrecthalfarcrotview.JoinRectHalfArcView

//import com.example.biboxrotfromeithersideview.BiBoxRotFromEitherSideView

//import com.example.rightanglelineenderview.RightAngleLineEnderView

//import com.example.barstopthenmoveview.BarStopThenMoveView

//import com.example.trilinedownjoinview.TriLineJoinDownView

//import com.example.linesquarejoindownview.LineSquareJoinDownView

//import com.example.brickbreaklineview.BrickBreakLineView

//import com.example.boxsmallboxdropview.BoxSmallBoxDropView

//import com.example.archalfrotdownview.ArcHalfDownView

//import com.example.erotdownview.ERotDownView

//import com.example.lineexpandrotatorview.LineExpandRotatorView

//import com.example.linetshiftdownview.LineTShiftDownView

//import com.example.minipiedownview.MiniPieDownView

//import com.example.altlinetreedownview.AllLineTreeDownView

//import com.example.crosstoplineview.CrossTopLineView

//import com.example.vdoorrotmoveview.VDoorRotMoveView

//import com.example.boxabovelineenclosureview.BoxAboveLineClosureView

//import com.example.linebibarview.LineBiBarView

//import com.example.linebartravellerview.LineBarTravellerView

//import com.example.bararcextenderview.BarArcExtenderView

//import com.example.trapeshooterupview.TrapeShooterUpView

//import com.example.linepeakrotmoveview.LinePeakRotMoveView

//import com.example.vlineencloserview.VLineEncloserView

//import com.example.quarterarcfillview.QuarterArcFillView

//import com.example.eithersidelbarview.EitherSideLBarView

//import com.example.squarecreateshrinkview.SquareCreateShrinkView

//import com.example.lcreatearcdownview.LCreateArcDownView

//import com.example.arcfromuptosideview.ArcFromUpToSideView

//import com.example.linefromuptodownview.LineFromUpToDownView

//import com.example.barjointogoupview.BarJoinToGoUpView

//import com.example.hlinetravellerview.HLineTravellerView

//import com.example.circlequadlineview.CircleQuadLineView

//import com.example.trilinearcdownview.TriLineArcDownView

//import com.example.linedividebarmoveview.LineDivideBarMoveView

//import com.example.arcdividerlineview.ArcDividerLineView

//import com.example.direclinescreateview.DirecLinesCreateView

//import com.example.bigbarsmalltopview.BigBarSmallTopView

//import com.example.sharelinemoverview.ShareLineMoverView

//import com.example.linetakenbylinesview.LineTakenByLinesView

//import com.example.arrowlinemakerupview.ArrowLineMakerUpView
//import com.example.linetomultisquaredownview.LineToMultiSquareDownView

//import com.example.linetoarrowdroplineview.LineToArrowDropLineView

//import com.example.downloadiconmoveview.DownloadIconMoveView

//import com.example.powerbuttonshooterview.PowerButtonShooterView

//import com.example.twoconccircdownview.TwoConcCircleDownView

//import com.example.squarejourneymovementview.SquareJourneyMovementView

//import com.example.barcompletegoupview.BarCompleteGoUpView

//import com.example.rotrectdividegoview.RotRectDivideView

//import com.example.mushroomlinearcview.MushroomLineArcView

//import com.example.rectjointhenbreakview.RectBreakThenJoinView

//import com.example.wavesweeparcdropview.WaveArcSweepDropView

//import com.example.linedroptobreakview.LineDropToBreakView

//import com.example.breaktopsquaredropview.BreakTopSquareDropView

//import com.example.breakarcrotdropview.BreakArcToDropView

//import com.example.piemirrorstepdropview.PieMirrorStepDropView

//import com.example.linearrowrotmoveview.LineArrowRotMoveView

//import com.example.squarebreakthenjoinview.SquareBreakThenJoinView

//import com.example.bidividesquaredropview.BiDivideSquareDropView

//import com.example.bisquarejoincreateview.BiSquareJoinCreateView

//import com.example.squaresemicircledownview.SquareSemiCircleDownView

//import com.example.fourlineoppositedragview.FourLineOppositeDragView

//import com.example.rightanglesquaredivideview.RightAngleSquareDivideView

//import com.example.bilinehdropview.BiLineHDropView

//import com.example.linerotsqdowndivideview.LineRotSqDownDivideView

//import com.example.circlesdroptosweepview.CirclesDropToSweepView

//import com.example.ballsdropintosweepview.BallsDropIntoSweepView

//import com.example.linerotatedroprectview.LineRotateDropRectView

//import com.example.sidelineupmovedview.SideLineUpMovedView

//import com.example.vlinedropview.VLineDropView

//import com.example.trilinecreateendview.TriLineCreateEndView

//import com.example.dropsquaremovetosideview.DropSquareMoveToSideView

//import com.example.rotatelinethendropview.RotateThenLineDropView

//import com.example.lineattachdroplineview.LineAttachLineDropView

//import com.example.linebardropendview.LineBarDropEndView

//import com.example.circularsqjoinerdropview.CircularSqJoinerDropView

//import com.example.halfarccompletiondropview.HalfArcCompletionDropView

//import com.example.dropsquaretorightview.DropSquareToLeftView

//import com.example.squareshooterballview.SquareShooterBallView

//import com.example.balldownfromdiagview.BallDownFromDiagView

//import com.example.llinearcview.LLineArcView

//import com.example.twolinetosquaredownview.TwoLineSquareDownView

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
        //TwoLineSquareDownView.create(this)
        //LLineArcView.create(this)
        //BallDownFromDiagView.create(this)
        //SquareShooterBallView.create(this)
        //DropSquareToLeftView.create(this)
        //HalfArcCompletionDropView.create(this)
        //CircularSqJoinerDropView.create(this)
        //LineBarDropEndView.create(this)
        //LineAttachLineDropView.create(this)
        //RotateThenLineDropView.create(this)
        //DropSquareMoveToSideView.create(this)
        //TriLineCreateEndView.create(this)
        //VLineDropView.create(this)
        //SideLineUpMovedView.create(this)
        //LineRotateDropRectView.create(this)
        //BallsDropIntoSweepView.create(this)
        //CirclesDropToSweepView.create(this)
        //LineRotSqDownDivideView.create(this)
        //BiLineHDropView.create(this)
        //RightAngleSquareDivideView.create(this)
        //FourLineOppositeDragView.create(this)
        //SquareSemiCircleDownView.create(this)
        //BiSquareJoinCreateView.create(this)
        //BiDivideSquareDropView.create(this)
        //SquareBreakThenJoinView.create(this)
        //LineArrowRotMoveView.create(this)
        //PieMirrorStepDropView.create(this)
        //BreakArcToDropView.create(this)
        //BreakTopSquareDropView.create(this)
        //LineDropToBreakView.create(this)
        //WaveArcSweepDropView.create(this)
        //RectBreakThenJoinView.create(this)
        //MushroomLineArcView.create(this)
        //RotRectDivideView.create(this)
        //BarCompleteGoUpView.create(this)
        //SquareJourneyMovementView.create(this)
        //TwoConcCircleDownView.create(this)
        //PowerButtonShooterView.create(this)
        //DownloadIconMoveView.create(this)
        //LineToArrowDropLineView.create(this)
        //LineToMultiSquareDownView.create(this)
        //ArrowLineMakerUpView.create(this)
        //LineTakenByLinesView.create(this)
        //ShareLineMoverView.create(this)
        //BigBarSmallTopView.create(this)
        //DirecLinesCreateView.create(this)
        //ArcDividerLineView.create(this)
        //LineDivideBarMoveView.create(this)
        //TriLineArcDownView.create(this)
        //CircleQuadLineView.create(this)
        //HLineTravellerView.create(this)
        //BarJoinToGoUpView.create(this)
        //LineFromUpToDownView.create(this)
        //ArcFromUpToSideView.create(this)
        //LCreateArcDownView.create(this)
        //SquareCreateShrinkView.create(this)
        //EitherSideLBarView.create(this)
        //QuarterArcFillView.create(this)
        //VLineEncloserView.create(this)
        //LinePeakRotMoveView.create(this)
        //TrapeShooterUpView.create(this)
        //BarArcExtenderView.create(this)
        //LineBarTravellerView.create(this)
        //LineBiBarView.create(this)
        //BoxAboveLineClosureView.create(this)
        //VDoorRotMoveView.create(this)
        //CrossTopLineView.create(this)
        //AllLineTreeDownView.create(this)
        //MiniPieDownView.create(this)
        //LineTShiftDownView.create(this)
        //LineExpandRotatorView.create(this)
        //ERotDownView.create(this)
        //ArcHalfDownView.create(this)
        //BoxSmallBoxDropView.create(this)
        //BrickBreakLineView.create(this)
        //LineSquareJoinDownView.create(this)
        //TriLineJoinDownView.create(this)
        //BarStopThenMoveView.create(this)
        //RightAngleLineEnderView.create(this)
        //BiBoxRotFromEitherSideView.create(this)
        //JoinRectHalfArcView.create(this)
        //TLineSideDownView.create(this)
        //ParallelLineToVView.create(this)
        //BoxLineStandDownView.create(this)
        //ArrowHeadShiftView.create(this)
        //PlusCrossRotDownView.create(this)
        //SquareBallCatcherView.create(this)
        //LineSplitBarView.create(this)
        //WheelArcBarView.create(this)
        //AltSquareFillUpView.create(this)
        //ArcLineDirecView.create(this)
        //ParallelLineTravellerView.create(this)
        //TargetArcLineView.create(this)
        //HalfArcJoinDownView.create(this)
        //ArcSweepFromSideView.create(this)
        //HundredOneRotView.create(this)
        //SquareToHRotView.create(this)
        //HalfArcLineVaseView.create(this)
        //UmbreallaArcLineView.create(this)
        //EnclosedAltSideBarView.create(this)
        //DivideBarUpView.create(this)
        //DivideBallUpView.create(this)
        //BarSideUpgradeView.create(this)
        //ConcentricCircleSideWiseView.create(this)
        //TriBarOverLineView.create(this)
        //BarHammerDivideView.create(this)
        //ArrowSignShootView.create(this)
        //BoxUpToDownOnlineView.create(this)
        //BiLineRotateVView.create(this)
        //SemiArcBarHolderView.create(this)
        //HalfBarRotDownView.create(this)
        //TrapeBarJoinView.create(this)
        //ArcBarRotDownView.create(this)
        //SquareExtenderLineView.create(this)
        //PeakSquareHolderView.create(this)
        //LineToTriMoveUpView.create(this)
        //LineBallMidRotView.create(this)
        //CloseOpenBoxView.create(this)
        //BarPushLineUpView.create(this)
        //ArrowLeggedMoverView.create(this)
        //UpRectWingsView.create(this)
        //LineSideRotView.create(this)
        //SideRectRotLineView.create(this)
        //OpenBoxCrossMoverView.create(this)
        //SideBarArmRotView.create(this)
        //KRotTView.create(this)
        //DiagBoatLineView.create(this)
        //OpenLineBoxLineView.create(this)
        //LineRotToSideNView.create(this)
        //ClipBoxLineRotView.create(this)
        //LineFromEndVSweepView.create(this)
        //LineArcHookView.create(this)
        //MultipleTimesRotArcView.create(this)
        //SideWiseRotFillRectView.create(this)
        //AltSideRotRectUpView.create(this)
        val colors : Array<String> = arrayOf(
            "#1A237E",
            "#00C853",
            "#AA00FF",
            "#01579B",
            "#BF360C"
        )
        //HammerBarHitLeftView.create(this)
        //ExclaimShooterView.create(this)
        //ExpandHalfArcLineView.create(this)
        //LinkedLineBoxesView.create(this)
        //LinkedCircleRotatorView.create(this)
        //BothDirectionArrowsView.create(this)
        //LineRotRectExpanderView.create(this)
        //SquareSideLineRotView.create(this)
        //
        // HorizToVertRectCatcherView.create(this)
        //InvertedLineToRectView.create(this)
        QuarterLineRotRectUpView.create(this)
        fullScreen()
    }
}


fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}