package com.samuelnunes.domain.use_case

import com.samuelnunes.domain.entity.Breed
import com.samuelnunes.domain.repository.ICatsRepository
import com.samuelnunes.utility_tool_kit.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCatsGifsUseCase @Inject constructor(
    private var repository: ICatsRepository
) {

    operator fun invoke(): Flow<Result<List<Breed.Image>>> {
        return repository.getCatsGifs().map {
            it.map { imageList ->
                imageList?.map { dto ->
                    dto.toImage()
                }
            }
        }
    }

}