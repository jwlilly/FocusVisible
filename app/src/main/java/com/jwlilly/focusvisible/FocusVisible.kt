package com.jwlilly.focusvisible

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.graphics.PixelFormat
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent


class FocusVisible() : AccessibilityService() {

    private lateinit var mLayout : HighlightOverlay

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        when(event?.eventType) {
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                val info = event.source
                val rect = Rect()
                info?.getBoundsInScreen(rect)
                mLayout.setOverlayRect(rect)
            }
            AccessibilityEvent.TYPE_WINDOWS_CHANGED -> {
                Log.d("FOCUS_VISIBLE", "onAccessibilityEvent: TYPE_WINDOWS_CHANGED")
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                Log.d("FOCUS_VISIBLE", "onAccessibilityEvent: TYPE_WINDOWS_CONTENT_CHANGED")
            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                Log.d("FOCUS_VISIBLE", "onAccessibilityEvent: TYPE_WINDOWS_STATE_CHANGED")
            }
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_START -> {
                Log.d("FOCUS_VISIBLE", "onAccessibilityEvent: TYPE_TOUCH_INTERACTION_START")
            }
        }
    }

    override fun onInterrupt() {

    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = AccessibilityServiceInfo()
        info.flags = (AccessibilityServiceInfo.DEFAULT
                or AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
                or AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT)
        info.eventTypes = (AccessibilityEvent.TYPE_VIEW_FOCUSED
                or AccessibilityEvent.TYPE_WINDOWS_CHANGED
                or AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                or AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                or AccessibilityEvent.TYPE_VIEW_SELECTED
                or AccessibilityEvent.TYPE_TOUCH_INTERACTION_START)
        this.serviceInfo = info;
        initOverlay()
    }

    private fun initOverlay() {
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        mLayout = HighlightOverlay(this)
        val lp = WindowManager.LayoutParams()
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        lp.format = PixelFormat.TRANSLUCENT

        lp.flags =
            lp.flags or (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        val inflater = LayoutInflater.from(this)

        inflater.inflate(R.layout.focus_outline, mLayout)
        mLayout.setAlpha(0.75f)
        wm.addView(mLayout, lp)
    }
    private fun stillHaveFocus() : Boolean {
        return false
    }
}