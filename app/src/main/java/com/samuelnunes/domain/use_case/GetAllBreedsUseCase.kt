package com.samuelnunes.domain.use_case

import com.samuelnunes.domain.entity.Breed
import com.samuelnunes.domain.repository.ICatsRepository
import com.samuelnunes.utility_tool_kit.domain.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllBreedsUseCase @Inject constructor(
    private var repository: ICatsRepository
) {
    operator fun invoke(isAsc: Boolean = true): Flow<Resource<List<Breed>>> {
        return repository.getAllBreeds(isAsc).map { res ->
            res.map(mapperSuccess = { content ->
                content?.map {
                    it.toBreed()
                }
            })
        }
    }

}