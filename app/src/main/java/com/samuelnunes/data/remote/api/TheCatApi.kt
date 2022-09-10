package com.samuelnunes.data.remote.api

import com.samuelnunes.data.dto.request.query.OrderImages
import com.samuelnunes.data.dto.request.query.SizeImages
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
    suspend fun getAllBreeds(
        @Query("limit") limit: Int? = 10,
        @Query("page") page: Int? = 0
    ): Response<List<BreedDTO>>

    @GET("images/search")
    @ErrorType(NOT_FOUND, NotFoundError::class)
    fun getRandomImage(
        @Query("size") size: SizeImages? = null,
        @Query("mime_types") mimeTypes: TypeImages? = null,
        @Query("order") order: OrderImages? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("has_breeds") hasBoolean: Boolean? = null,
        @Query("breed_ids") breedIds: Int? = null,
        @Query("category_ids") category: Int? = null,
        @Query("sub_id") subId: Int? = null
    ): Flow<Result<List<BreedDTO.ImageDTO>>>

}
