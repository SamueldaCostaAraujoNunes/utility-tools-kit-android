package com.samuelnunes.domain.repository

import com.samuelnunes.data.dto.response.BreedDTO
import com.samuelnunes.domain.use_case.GetAllBreedsUseCase
import com.samuelnunes.utility_tool_kit.domain.Result
import kotlinx.coroutines.flow.Flow

interface ICatsRepository {

    fun getBreed(id: String): Flow<Result<BreedDTO>>
    fun getAllBreeds(isAsc: Boolean): Flow<Result<List<BreedDTO>>>

    fun getCatsGifs(): Flow<Result<List<BreedDTO.ImageDTO>>>
}