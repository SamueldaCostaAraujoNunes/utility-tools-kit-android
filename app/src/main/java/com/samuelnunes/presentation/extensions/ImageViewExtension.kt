package com.samuelnunes.presentation.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("imageByUrl")
fun imageByUrl(iv: ImageView, url: String?) = iv.load(url)