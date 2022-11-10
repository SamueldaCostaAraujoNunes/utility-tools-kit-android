package com.samuelnunes.domain.repository

import com.samuelnunes.data.dto.response.BreedDTO
import com.samuelnunes.utility_tool_kit.domain.Resource
import kotlinx.coroutines.flow.Flow

interface ICatsRepository {

    fun getBreed(id: String): Flow<Resource<BreedDTO>>
    fun getAllBreeds(isAsc: Boolean): Flow<Resource<List<BreedDTO>>>

    fun getCatsGifs(): Flow<Resource<List<BreedDTO.ImageDTO>>>
}