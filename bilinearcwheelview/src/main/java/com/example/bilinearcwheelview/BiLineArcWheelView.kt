package com.example.bilinearcwheelview

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.content.Context
import android.app.Activity

val colors : Array<Int> = arrayOf(
    "#1A237E",
    "#EF5350",
    "#AA00FF",
    "#C51162",
    "#00C853"
).map {
    Color.parseColor(it)
}.toTypedArray()
val parts : Int = 4
val scGap : Float = 0.04f / parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 7.9f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")
val rot : Float = 90f
val sweep : Float = 180f


fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n


fun Canvas.drawBiLineArcWheel(scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    save()
    translate(w / 2 + (w / 2 + size) * sc4, h / 2)
    rotate(sweep * sc4)
    for (j in 0..1) {
        save()
        scale(1f, 1f - 2 * j)
        save()
        rotate(sweep * sc3 * (1 - 2 * j))
        translate(0f, h / 2 - (h / 2 - size) * sc2)
        drawLine(0f, 0f, 0f, -size * sc1, paint)
        restore()
        restore()
        drawArc(RectF(-size, -size, size, size), rot + sweep * j,  sweep * sc3, false, paint)
    }
    restore()
}

fun Canvas.drawBLAWNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.style = Paint.Style.STROKE
    drawBiLineArcWheel(scale, w, h, paint)
}

class BiLineArcWheelView(ctx : Context) : View(ctx) {

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
                view.invalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class BLAWNode(var i : Int, val state : State = State()) {

        private var next : BLAWNode? = null
        private var prev : BLAWNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = BLAWNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawBLAWNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : BLAWNode {
            var curr : BLAWNode? = prev
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

    data class BiLineArcWheel(var i : Int) {

        private var curr : BLAWNode = BLAWNode(0)
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

    data class Renderer(var view : BiLineArcWheelView) {

        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val blaw : BiLineArcWheel = BiLineArcWheel(0)
        private val animator : Animator = Animator(view)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            blaw.draw(canvas, paint)
            animator.animate {
                blaw.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            blaw.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : BiLineArcWheelView {
            val view : BiLineArcWheelView = BiLineArcWheelView(activity)
            activity.setContentView(view)
            return view
        }
    }
}