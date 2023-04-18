package com.samuelnunes

import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.samuelnunes.utils.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ToolKitUtilsApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun newImageLoader(): ImageLoader = buildImageLoader(this) {
        buildMemoryCache(this@ToolKitUtilsApplication) {
            maxSizePercent(0.25)
        }
        buildDiskCache {
            directory(cacheDir.resolve("image_cache"))
            maxSizePercent(0.02)
        }
        components {
            add(ImageDecoderDecoder.Factory())
        }
    }

    private inline fun buildImageLoader(
        context: Context,
        builderAction: ImageLoader.Builder.() -> Unit = {}
    ): ImageLoader =
        ImageLoader.Builder(context).apply(builderAction).build()

    private inline fun ImageLoader.Builder.buildMemoryCache(
        context: Context,
        builderAction: MemoryCache.Builder.() -> Unit = {}
    ) = memoryCache(MemoryCache.Builder(context).apply(builderAction).build())

    private inline fun ImageLoader.Builder.buildDiskCache(
        builderAction: DiskCache.Builder.() -> Unit = {}
    ) = diskCache(DiskCache.Builder().apply(builderAction).build())


}