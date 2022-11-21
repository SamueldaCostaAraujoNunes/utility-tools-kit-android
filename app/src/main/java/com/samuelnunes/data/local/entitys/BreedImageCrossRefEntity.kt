package com.samuelnunes.data.local.entitys

import androidx.room.Entity

@Entity(primaryKeys = ["breedId", "imageId"])
data class BreedImageCrossRefEntity(
    val breedId: String,
    val imageId: String
)