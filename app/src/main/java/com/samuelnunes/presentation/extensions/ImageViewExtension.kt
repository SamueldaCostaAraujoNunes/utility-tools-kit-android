package com.samuelnunes.presentation.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.samuelnunes.domain.entity.Breed

@BindingAdapter("imageByUrl")
fun imageByUrl(iv: ImageView, url: String?) = iv.load(url)

@BindingAdapter("imageByUrl")
fun imageByUrl(iv: ImageView, urls: List<Breed.Image>) {
    if(urls.isNotEmpty()) {
        iv.load(urls.map { it.url }.random())
    }
}