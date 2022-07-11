package com.samuelnunes.data.repository

import com.samuelnunes.data.remote.api.TheCatApi
import com.samuelnunes.data.remote.dto.BreedDTO
import com.samuelnunes.domain.repository.ICatsRepository
import com.samuelnunes.utility_tool_kit.domain.Result
import com.samuelnunes.utility_tool_kit.network.BaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CatsRepository @Inject constructor(private val api: TheCatApi): BaseRepository(), ICatsRepository {

    override fun getAllBreeds(): Flow<Result<List<BreedDTO>>> = api.getAllBreeds()

    override fun getCatsGifs(): Flow<Result<List<BreedDTO.ImageDTO>>> = api.getRandomGif()

}