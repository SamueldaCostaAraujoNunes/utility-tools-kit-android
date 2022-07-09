package com.samuelnunes.utility_tool_kit.extensions

import com.samuelnunes.utility_tool_kit.utils.UiText

fun String.toUiText() = UiText.DynamicString(this)