package com.samuelnunes.utility_tool_kit.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.Transformations.map

sealed class UiText {

    data class DynamicString(val value: String): UiText()
    class StringResource(
        @StringRes val id: Int,
        vararg args: String
    ) : UiText() {
        val values: Array<out String> = args
    }

    override fun toString(): String {
        return if(this is DynamicString) {
            value
        } else {
            super.toString()
        }
    }

    fun toString(context: Context): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> context.getString(id, *values)
        }
    }
}

fun String.toUiText() = UiText.DynamicString(this)

fun Throwable.toUiText() = UiText.DynamicString(localizedMessage ?: message ?: toString())