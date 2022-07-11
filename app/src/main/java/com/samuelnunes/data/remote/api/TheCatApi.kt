package com.samuelnunes.data.remote.api

import com.samuelnunes.data.remote.dto.BreedDTO
import com.samuelnunes.utility_tool_kit.domain.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface TheCatApi {

    @GET("breeds")
    fun getAllBreeds(): Flow<Result<List<BreedDTO>>>

    @GET("images/search?mime_types=gif")
    fun getRandomGif(): Flow<Result<List<BreedDTO.ImageDTO>>>

}