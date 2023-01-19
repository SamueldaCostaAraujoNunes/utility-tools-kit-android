package com.samuelnunes.domain.repository

import com.samuelnunes.data.local.entitys.BreedWithImage
import com.samuelnunes.data.local.entitys.ImageEntity
import com.samuelnunes.utility_tool_kit.domain.Resource
import kotlinx.coroutines.flow.Flow

interface ICatsRepository {

    fun getBreed(id: String): Flow<Resource<BreedWithImage>>
    fun getAllBreeds(isAsc: Boolean): Flow<Resource<List<BreedWithImage>>>
    fun getCatsGifs(): Flow<Resource<List<ImageEntity>>>
    suspend fun fetchImagesBreed(breedId: String)
}