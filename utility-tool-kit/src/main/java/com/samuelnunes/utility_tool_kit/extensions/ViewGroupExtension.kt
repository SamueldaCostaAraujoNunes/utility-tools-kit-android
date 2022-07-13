package com.samuelnunes.utility_tool_kit.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlinx.coroutines.NonDisposableHandle.parent

inline fun <reified T : ViewDataBinding> ViewGroup.inflate(@LayoutRes res: Int, attachToParent: Boolean = false): T {
    return DataBindingUtil.inflate(LayoutInflater.from(context), res, this, attachToParent)
}

val ViewGroup.inflater: LayoutInflater
    get() = LayoutInflater.from(context)

