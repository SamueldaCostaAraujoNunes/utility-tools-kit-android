package com.samuelnunes.domain.use_case

import com.samuelnunes.domain.entity.Breed
import com.samuelnunes.domain.repository.ICatsRepository
import com.samuelnunes.utility_tool_kit.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllBreedsUseCase @Inject constructor(
    private var repository: ICatsRepository
) {

    operator fun invoke(): Flow<Result<List<Breed>>> {
        return repository.getAllBreeds().map { res ->
            res.map { content ->
                content?.map {
                    it.toBreed()
                }
            }
        }
    }

}