package com.samuelnunes.utility_tool_kit.extensions

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.samuelnunes.utility_tool_kit.utils.UiText

@BindingAdapter("error")
fun TextInputLayout.error(uiText: UiText?) {
    error(uiText?.toString(context))
}

@BindingAdapter("error")
fun TextInputLayout.error(message: String?) {
    if (message == null) {
        isErrorEnabled = false
        this.error = null
    } else {
        isErrorEnabled = true
        this.error = message
    }
}