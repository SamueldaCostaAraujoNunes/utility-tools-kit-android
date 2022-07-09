package com.samuelnunes.utility_tool_kit.binding

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleIf")
fun View.visibleIf(condition: Boolean) {
    visibility = if(condition) View.VISIBLE else View.GONE
}

@BindingAdapter("goneIf")
fun View.goneIf(condition: Boolean) {
    visibility = if(condition) View.GONE else View.VISIBLE
}