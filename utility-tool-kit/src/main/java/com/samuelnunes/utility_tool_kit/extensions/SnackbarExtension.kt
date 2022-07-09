package com.samuelnunes.utility_tool_kit.extensions

import android.os.Build
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar

fun Snackbar.setFontFamily(@FontRes font: Int): Snackbar {
    val tv = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        tv.typeface = context.resources.getFont(font)
    }
    return this
}