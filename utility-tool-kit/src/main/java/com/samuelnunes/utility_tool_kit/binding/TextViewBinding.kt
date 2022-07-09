package com.samuelnunes.utility_tool_kit.binding

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.samuelnunes.utility_tool_kit.utils.UiText

@BindingAdapter(value = ["fadeAnimationText", "durationAnimation"], requireAll = false)
fun TextView.fadeAnimationText(newText: UiText?, durationAnimation: Long = 275) {
    fadeAnimationText(newText?.toString(context), durationAnimation)
}

@BindingAdapter(value = ["fadeAnimationText", "durationAnimation"], requireAll = false)
fun TextView.fadeAnimationText(newText: String?, durationAnimation: Long = 275) {
    if (newText != null) {
        val anim = AlphaAnimation(1.0f, 0.0f)
        anim.duration = durationAnimation
        anim.repeatCount = 1
        anim.repeatMode = Animation.REVERSE

        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {
                text = newText
            }
        })
        startAnimation(anim)

    } else {
        text = newText
    }
}

@BindingAdapter("android:text")
fun TextView.text(uiText: UiText?) {
    text = uiText?.toString(context)
}
