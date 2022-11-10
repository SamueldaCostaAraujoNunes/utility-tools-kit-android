package com.samuelnunes.utility_tool_kit.network

import com.samuelnunes.utility_tool_kit.domain.Resource
import kotlinx.coroutines.flow.*
import retrofit2.Response


abstract class BaseRepository {


    inline fun <LocalType, RemoteType> networkBoundResource(
        crossinline query: () -> Flow<LocalType>,
        crossinline fetch: suspend () -> Response<RemoteType>,
        crossinline saveFetchResult: suspend (RemoteType) -> Unit,
        crossinline onFetchFailed: (Throwable) -> Unit = { },
        crossinline shouldUpdate: (LocalType?) -> Boolean = { true }
    ): Flow<Resource<LocalType>> = flow {
        emit(Resource.Loading())
        val resultFlow = query()

        val firstOrNull = resultFlow.firstOrNull()

        if (firstOrNull != null) {
            resultFlow.map {
                Resource.Success(
                    data = it
                )
            }
        }

        if (shouldUpdate(firstOrNull)) {
            val remoteFlow = try {
                val response = fetch()
                val statusCode = response.code()
                if (response.isSuccessful) {
                    val body = response.body()

                    if (body == null || statusCode == 204) {
                        resultFlow.map {
                            Resource.Empty()
                        }
                    } else {
                        saveFetchResult(body)
                        resultFlow.map {
                            Resource.Success(
                                data = it,
                                statusCode = statusCode
                            )
                        }
                    }
                } else {
                    resultFlow.map {
                        Resource.Error(
                            message = response.message(),
                            dataInCache = it,
                            statusCode = statusCode
                        )
                    }
                }
            } catch (throwable: Throwable) {
                onFetchFailed(throwable)
                resultFlow.map {
                    Resource.Error(
                        dataInCache = it,
                        throwable = throwable
                    )
                }
            }
            emitAll(remoteFlow)
        }
    }

}


