package com.example.CanvasExperiment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import java.util.*
import kotlin.system.measureNanoTime

private const val STROKE_WIDTH = 12f

class MyCanvasView(context: Context) : View(context) {

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitMap: Bitmap

    private val backgroundColor = ResourcesCompat.getColor(
        resources, R.color.colorBackground, null)
    private val drawColor = ResourcesCompat.getColor(
        resources, R.color.colorPaint, null)

    private var path = Path()

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    private var currentX = 0f
    private var currentY = 0f

    private var debugTime = System.nanoTime()
    private var deltaTime = 0L

    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    // Set up the paint with which to draw.
    private val paint = Paint().apply {
        color = drawColor
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
    }

    // essentially, this will be the constructor function for the View
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::extraBitMap.isInitialized) extraBitMap.recycle()

        extraBitMap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitMap)
        extraCanvas.drawColor(backgroundColor)
    }

    // draw
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var now: Long = System.nanoTime()
        deltaTime = now - debugTime
        debugTime = now
        println(deltaTime.toFloat() / 1000000f)

        canvas.drawBitmap(extraBitMap, 0f, 0f, null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false

        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {

            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_UP   -> touchMove()
            MotionEvent.ACTION_UP   -> touchUp()

        }

        return true
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val dx = Math.abs(motionTouchEventX - currentX)/10
        val dy = Math.abs(motionTouchEventY - currentY)/10

        if (dx >= touchTolerance || dy >= touchTolerance) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(
                    currentX, currentY,
                    (motionTouchEventX + currentX) / 2,
                    (motionTouchEventY + currentY) / 2)

            currentX = motionTouchEventX
            currentY = motionTouchEventY

            // Draw the path in the extra bitmap to cache it.
            extraCanvas.drawPath(path, paint)
        }

        invalidate()
    }

    private fun touchUp() {
        path.reset()
    }

}