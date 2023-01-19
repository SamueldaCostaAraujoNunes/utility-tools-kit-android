package com.samuelnunes.data.repository

import com.samuelnunes.data.dto.request.query.TypeImages
import com.samuelnunes.data.local.dao.BreedDao
import com.samuelnunes.data.local.dao.CatsDao
import com.samuelnunes.data.local.dao.ImageDao
import com.samuelnunes.data.local.entitys.BreedWithImage
import com.samuelnunes.data.local.entitys.ImageEntity
import com.samuelnunes.data.remote.api.TheCatApi
import com.samuelnunes.domain.repository.ICatsRepository
import com.samuelnunes.utility_tool_kit.database.dao.insertOrUpdate
import com.samuelnunes.utility_tool_kit.domain.Resource
import com.samuelnunes.utility_tool_kit.network.BaseRepository
import kotlinx.coroutines.flow.Flow
import java.util.Collections.addAll
import javax.inject.Inject

class CatsRepository @Inject constructor(
    private val api: TheCatApi,
    private val catsDao: CatsDao,
    private val breedDao: BreedDao,
    private val imageDao: ImageDao
) : BaseRepository(), ICatsRepository {

    private suspend fun insertOrUpdateBreed(map: List<BreedWithImage>) {
        breedDao.insertOrUpdate(map.map { it.breedEntity })
        imageDao.insertOrUpdate(
            mutableListOf<ImageEntity>().apply {
                map.forEach { addAll(it.imageEntity) }
            }
        )
    }

    private suspend fun insertOrUpdateBreed(map: BreedWithImage) {
        breedDao.insertOrUpdate(map.breedEntity)
        imageDao.insertOrUpdate(map.imageEntity)
    }

    override fun getBreed(id: String): Flow<Resource<BreedWithImage>> =
        networkBoundResource(
            { catsDao.getBreed(id) },
            {
                fetchImagesBreed(id)
                api.getBreed(id)
            },
            ::insertOrUpdateBreed,
            { it.toBreedWithImage() }
        )

    override suspend fun fetchImagesBreed(breedId: String) {
        val resource = api.getImage(
            limit = 10,
            breedId = breedId
        )
        if (resource is Resource.Success) {
            val resultConverted = resource.data.map { it.toEntity(breedId) }
            imageDao.insertOrUpdate(resultConverted)
        }
    }

    override fun getAllBreeds(isAsc: Boolean): Flow<Resource<List<BreedWithImage>>> =
        networkBoundResource(
            if (isAsc) catsDao::getAllAsc else catsDao::getAllDesc,
            api::getAllBreeds,
            ::insertOrUpdateBreed,
            { list -> list.map { it.toBreedWithImage() } }
        )

    override fun getCatsGifs(): Flow<Resource<List<ImageEntity>>> = networkBoundResource(
        imageDao::getRandomGifs,
        { api.getImage(mimeTypes = TypeImages.GIF) },
        imageDao::insertOrUpdate,
        { list -> list.map { it.toEntity() } }
    )


}