package com.samuelnunes.domain.repository

import com.samuelnunes.data.dto.response.BreedDTO
import com.samuelnunes.utility_tool_kit.domain.Result
import kotlinx.coroutines.flow.Flow

interface ICatsRepository {

    fun getAllBreeds(): Flow<Result<List<BreedDTO>>>

    fun getCatsGifs(): Flow<Result<List<BreedDTO.ImageDTO>>>
}