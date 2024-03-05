package com.jwlilly.focusvisible

import android.content.Context
import android.graphics.BlendMode
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.RequiresApi

class HighlightOverlay : FrameLayout {
    private val borderPaint = Paint()
    private var overlayRect = Rect()
    private var notificationBarHeight = 0

    constructor(context: Context) : super(context) {
        setWillNotDraw(false)
        notifificationBarOffset
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setWillNotDraw(false)
        notifificationBarOffset
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setWillNotDraw(false)
        notifificationBarOffset
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        setWillNotDraw(false)
        notifificationBarOffset
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val refocusPaint = Paint()
        refocusPaint.color = Color.alpha(100)
        refocusPaint.blendMode = BlendMode.COLOR
        borderPaint.color = Color.RED
        borderPaint.style = Paint.Style.STROKE
        borderPaint.blendMode = BlendMode.DARKEN
        borderPaint.strokeWidth = 10f
        overlayRect.offset(0, -notificationBarHeight)
        val rectF = RectF(overlayRect)
        canvas.drawRoundRect(rectF, 5f, 5f, refocusPaint)
        canvas.drawRoundRect(rectF, 5f, 5f, borderPaint)
    }

    fun setOverlayRect(overlay: Rect) {
        overlayRect = overlay
        invalidate()
    }

    val notifificationBarOffset: Unit
        get() {
            var result = 0
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
                notificationBarHeight = result
            }
        }
}
