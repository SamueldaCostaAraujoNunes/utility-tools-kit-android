package com.samuelnunes.utility_tool_kit.extensions

import android.os.Handler
import android.os.Looper
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING

fun ViewPager2.autoScroll(delay: Long = 2000) {
    val handler = Handler(Looper.getMainLooper())

    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val currentSize = (adapter?.itemCount ?: 1) - 1
            handler.removeMessages(0)
            if (position < currentSize) {
                handler.postDelayed({ currentItem = position + 1 }, delay)
            } else if(position >= currentSize) {
                handler.postDelayed({ currentItem = 0 }, delay)
            }
        }
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            if (state == SCROLL_STATE_DRAGGING) handler.removeMessages(0)
        }
    })

}