package com.samuelnunes.data.dto.response

import androidx.room.ColumnInfo
import com.samuelnunes.data.dto.request.query.TypeImages
import com.samuelnunes.data.local.entitys.ImageEntity
import java.util.*

data class ImageResponse(
    val height: Int,
    @ColumnInfo(name = "id_image")
    val id: String,
    val url: String,
    val width: Int
) {
    fun toEntity(): ImageEntity {
        val imageType = try {
            TypeImages.valueOf(url.substringAfterLast(".").uppercase(Locale.getDefault()))
        } catch (ex: IllegalArgumentException) {
            TypeImages.JPG
        }
        return ImageEntity(
            id, url, imageType
        )
    }
}