package com.samuelnunes.utility_tool_kit.binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.samuelnunes.utility_tool_kit.debounce.debounce
import kotlinx.coroutines.MainScope

@BindingAdapter("visibleIf")
fun View.visibleIf(condition: Boolean) {
    visibility = if(condition) View.VISIBLE else View.GONE
}

@BindingAdapter("goneIf")
fun View.goneIf(condition: Boolean) {
    visibility = if(condition) View.GONE else View.VISIBLE
}

@BindingAdapter("android:onClick")
fun View.setDebounceListener(onClickListener: View.OnClickListener) {
    val clickWithDebounce: (view: View) -> Unit =
        debounce(scope = MainScope()) {
            onClickListener.onClick(it)
        }
    setOnClickListener(clickWithDebounce)
}