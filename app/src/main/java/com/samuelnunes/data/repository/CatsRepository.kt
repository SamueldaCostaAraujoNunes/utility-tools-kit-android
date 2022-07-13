package com.samuelnunes.data.repository

import com.samuelnunes.data.remote.api.TheCatApi
import com.samuelnunes.data.dto.response.BreedDTO
import com.samuelnunes.data.local.dao.CatsDao
import com.samuelnunes.domain.repository.ICatsRepository
import com.samuelnunes.utility_tool_kit.database.dao.insertOrUpdate
import com.samuelnunes.utility_tool_kit.domain.Result
import com.samuelnunes.utility_tool_kit.network.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CatsRepository @Inject constructor(
    private val api: TheCatApi,
    private val dao: CatsDao
) : BaseRepository(), ICatsRepository {

    override fun getAllBreeds(): Flow<Result<List<BreedDTO>>> =
        networkBoundResource(dao::getAll, api::getAllBreeds, dao::insertAll)

    override fun getCatsGifs(): Flow<Result<List<BreedDTO.ImageDTO>>> = api.getRandomGif()

}