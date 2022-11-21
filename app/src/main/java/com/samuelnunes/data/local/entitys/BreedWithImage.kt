package com.samuelnunes.data.local.entitys

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class BreedWithImage(
    @Embedded val breedEntity: BreedEntity,
    @Relation(
        parentColumn = "breedId",
        entityColumn = "imageId",
        associateBy = Junction(BreedImageCrossRefEntity::class)
    )
    val imageEntity: List<ImageEntity>
)