package com.samuelnunes.utility_tool_kit.binding

import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter
import com.samuelnunes.utility_tool_kit.debounce.debounce
import kotlinx.coroutines.MainScope

@BindingAdapter("android:onClick")
fun Button.setDebounceListener(onClickListener: View.OnClickListener) {
    val clickWithDebounce: (view: View) -> Unit =
        debounce(scope = MainScope()) {
            onClickListener.onClick(it)
        }
    setOnClickListener(clickWithDebounce)
}