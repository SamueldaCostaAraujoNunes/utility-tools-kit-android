package com.samuelnunes.data.remote.api

import com.samuelnunes.data.dto.request.query.TypeImages
import com.samuelnunes.data.dto.response.BreedDTO
import com.samuelnunes.data.dto.response.error.NotFoundError
import com.samuelnunes.utility_tool_kit.domain.HttpStatusCodeError.NOT_FOUND
import com.samuelnunes.utility_tool_kit.domain.Result
import com.samuelnunes.utility_tool_kit.network.ErrorType
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCatApi {

    @GET("breeds")
    suspend fun getAllBreeds(): Response<List<BreedDTO>>

    @GET("images/search")
    @ErrorType(NOT_FOUND, NotFoundError::class)
    fun getRandomImage(@Query("mime_types") mimeTypes: TypeImages): Flow<Result<List<BreedDTO.ImageDTO>>>

}
