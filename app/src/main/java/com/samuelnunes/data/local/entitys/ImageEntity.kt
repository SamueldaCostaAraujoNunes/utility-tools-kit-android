package com.samuelnunes.data.local.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samuelnunes.data.dto.request.query.TypeImages
import com.samuelnunes.domain.entity.Breed


@Entity
data class ImageEntity(
    @PrimaryKey
    val imageId: String,
    val url: String,
    val type: TypeImages
) {
    fun toImage(): Breed.Image = Breed.Image(
        imageId, url, type
    )
}