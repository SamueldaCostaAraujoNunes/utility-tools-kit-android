package com.samuelnunes.data.repository

import com.samuelnunes.data.dto.request.query.TypeImages
import com.samuelnunes.data.dto.response.BreedDTO
import com.samuelnunes.data.local.dao.CatsDao
import com.samuelnunes.data.remote.api.TheCatApi
import com.samuelnunes.domain.repository.ICatsRepository
import com.samuelnunes.utility_tool_kit.domain.Result
import com.samuelnunes.utility_tool_kit.network.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CatsRepository @Inject constructor(
    private val api: TheCatApi,
    private val dao: CatsDao
) : BaseRepository(), ICatsRepository {

    override fun getBreed(id: String): Flow<Result<BreedDTO>> = dao.getBreed(id).map { Result.Success(it) }

    override fun getAllBreeds(isAsc: Boolean): Flow<Result<List<BreedDTO>>> = networkBoundResource(
        if (isAsc) dao::getAllAsc else dao::getAllDesc,
        api::getAllBreeds,
        dao::insertAll
    )

    override fun getCatsGifs(): Flow<Result<List<BreedDTO.ImageDTO>>> =
        api.getRandomImage(mimeTypes = TypeImages.GIF)

}