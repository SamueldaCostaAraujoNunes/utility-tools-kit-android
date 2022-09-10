package com.samuelnunes.utility_tool_kit.binding


import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import jp.wasabeef.glide.transformations.BlurTransformation
import timber.log.Timber

@BindingAdapter(
    value = [
        "imageUrl",
        "imageUri",
        "clipCircle",
        "listener",
        "dontTransform",
        "blurImage",
        "blurRadius",
        "blurSampling",
        "fallbackResource",
        "crossFade"
    ],
    requireAll = false
)
fun imageUrl(
    imageView: ImageView,
    imageUrl: String?,
    imageUri: Uri?,
    clipCircle: Boolean?,
    listener: RequestListener<Drawable>?,
    dontTransform: Boolean?,
    blurImage: Boolean?,
    blurRadius: Int?,
    blurSampling: Int?,
    fallbackResource: Int?,
    crossFade: Boolean?
) {
    val load = imageUrl ?: imageUri ?: return
    var request = Glide.with(imageView).load(load)
    if (clipCircle == true) request = request.circleCrop()
    if (dontTransform == true) request = request.dontTransform()
    if (fallbackResource != null) request = request.error(fallbackResource)
    if (blurImage == true)
        request = request.transform(BlurTransformation(blurRadius ?: 15, blurSampling ?: 3))

    if (crossFade == true) request = request.transition(DrawableTransitionOptions.withCrossFade())

    if (listener != null) {
        request = request.listener(object : RequestListener<Drawable> {
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return listener.onResourceReady(resource, model, target, dataSource, isFirstResource)
            }

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return listener.onLoadFailed(e, model, target, isFirstResource)
            }
        })
    }
    request.into(imageView)
}