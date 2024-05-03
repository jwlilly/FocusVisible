package com.jwlilly.focusvisible

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.graphics.PixelFormat
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo


class FocusVisible : AccessibilityService() {

    private lateinit var mLayout : HighlightOverlay

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        when(event?.eventType) {
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                val info = event.source
                val rect = Rect()
                info?.getBoundsInScreen(rect)
                mLayout.setOverlayRect(rect)
            }

            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                val node = event.source?.let { getFocusedNode(it) };
                val rect = Rect()
                node?.getBoundsInScreen(rect)
                mLayout.setOverlayRect(rect)
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
        info.notificationTimeout = 150
        info.flags = (AccessibilityServiceInfo.DEFAULT
                or AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
                or AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT
                or AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS)
        info.eventTypes = (AccessibilityEvent.TYPE_VIEW_FOCUSED
                or AccessibilityEvent.TYPE_WINDOWS_CHANGED
                or AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                or AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                or AccessibilityEvent.TYPE_VIEW_SELECTED
                or AccessibilityEvent.TYPE_TOUCH_INTERACTION_START)
        this.serviceInfo = info
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
        mLayout.alpha = 0.75f
        wm.addView(mLayout, lp)
    }

    private fun getFocusedNode(rootNode : AccessibilityNodeInfo) : AccessibilityNodeInfo {
        try {
            if(rootNode.isFocused) {
                return rootNode
            }
            for(i in 0..rootNode.childCount) {
                return getFocusedNode(rootNode.getChild(i))
            }
        } catch (exception : Exception) {
            Log.d("FOCUS_VISIBLE", "Error finding focused node")
            return rootNode
        }
        return rootNode
    }
}