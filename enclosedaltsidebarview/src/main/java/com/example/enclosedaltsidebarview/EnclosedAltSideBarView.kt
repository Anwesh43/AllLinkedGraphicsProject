package com.example.enclosedaltsidebarview

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Color
import android.graphics.RectF
import android.graphics.Canvas

val colors : Array<Int> = arrayOf(
    "#01579B",
    "#D50000",
    "#DD2C00",
    "#2962FF",
    "#00838F"
).map {
    Color.parseColor(it)
}.toTypedArray()
val delay : Long = 20
val sizeFactor : Float = 3.2f
val barSizeFactor : Float = 6.3f
val strokeFactor : Float = 90f
val parts : Int = 6
val scGap : Float = 0.04f / parts
val deg : Float = 90f
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawEnclosedAltSideBar(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    val sc6 : Float = scale.divideScale(5, parts)
    val barSize : Float = Math.min(w, h) / barSizeFactor
    save()
    translate(w / 2 + (w / 2 + size) * sc6, h / 2)
    rotate(deg * sc5)
    for (j in 0..1) {
        save()
        scale(1f - 2 * j, 1f)
        save()
        save()
        translate(-size * sc3, 0f)
        drawLine(0f, 0f, 0f, -barSize * 0.25f * sc1 - (size - barSize * 0.25f) * sc4, paint)
        restore()
        save()
        translate(-(size - barSize * 0.5f) * sc3, -(size - barSize * 0.25f) * sc4)
        drawRect(RectF(-barSize * 0.5f * sc2, -barSize / 4, 0f, 0f), paint)
        restore()
        save()
        drawLine(0f, 0f, -barSize * 0.5f * sc2 - (size - barSize * 0.5f) * sc3, 0f, paint)
        restore()
        restore()
    }
    restore()
}

fun Canvas.drawEASBNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawEnclosedAltSideBar(scale, w, h, paint)
}

class EnclosedAltSideBarView(ctx : Context) : View(ctx) {

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class EASBNode(var i : Int, val state : State = State()) {

        private var next : EASBNode? = null
        private var prev : EASBNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = EASBNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawEASBNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : EASBNode {
            var curr : EASBNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class EnclosedAltSideBar(var i : Int) {

        private var curr : EASBNode = EASBNode(0)
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : EnclosedAltSideBarView) {

        private val easb : EnclosedAltSideBar = EnclosedAltSideBar(0)
        private val animator : Animator = Animator(view)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            easb.draw(canvas, paint)
            animator.animate {
                easb.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            easb.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : EnclosedAltSideBarView {
            val view : EnclosedAltSideBarView = EnclosedAltSideBarView(activity)
            activity.setContentView(view)
            return view
        }
    }
}