package com.samuelnunes.domain.use_case

import com.samuelnunes.data.local.entitys.ImageEntity
import com.samuelnunes.domain.entity.Breed
import com.samuelnunes.domain.repository.ICatsRepository
import com.samuelnunes.utility_tool_kit.domain.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBreedUseCase @Inject constructor(
    private var repository: ICatsRepository
) {
    operator fun invoke(id: String): Flow<Resource<Breed>> {
        return repository.getBreed(id).map { result ->
            result.map(mapperSuccess = { map ->
                val imageList = map.imageEntity.map(ImageEntity::toImage).take(10)
                map.breedEntity.toBreed(imageList)
            })
        }
    }

}