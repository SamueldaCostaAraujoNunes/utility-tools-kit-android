package com.samuelnunes.data.remote.api

import com.samuelnunes.data.dto.response.BreedDTO
import com.samuelnunes.utility_tool_kit.domain.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET

interface TheCatApi {

    @GET("breeds")
    suspend fun getAllBreeds(): Response<List<BreedDTO>>

    @GET("images/search?mime_types=gif")
    fun getRandomGif(): Flow<Result<List<BreedDTO.ImageDTO>>>

}