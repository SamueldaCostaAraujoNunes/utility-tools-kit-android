package com.samuelnunes.utility_tool_kit.network

import com.samuelnunes.utility_tool_kit.domain.Resource
import kotlinx.coroutines.flow.*
import retrofit2.Response


abstract class BaseRepository {

    inline fun <LocalType, RemoteType> networkBoundResource(
        crossinline query: () -> Flow<LocalType>,
        crossinline fetch: suspend () -> Resource<RemoteType>,
        crossinline saveFetchResult: suspend (RemoteType) -> Unit,
        crossinline onFetchFailed: (Throwable) -> Unit = { },
        crossinline shouldUpdate: (LocalType?) -> Boolean = { true }
    ): Flow<Resource<LocalType>> = flow {
        emit(Resource.Loading())
        val localFlow = query()

        val firstOrNull = localFlow.firstOrNull()

        if (shouldUpdate(firstOrNull)) {
            if (firstOrNull != null) {
                emit(Resource.Success(data = firstOrNull))
            }
            when (val resultFetch = fetch()) {
                is Resource.Success<RemoteType> -> {
                    saveFetchResult(resultFetch.data)
                }
                is Resource.Error -> {
                    onFetchFailed(resultFetch.exception)
                    emit(resultFetch.map())
                }
                else -> {}
            }
            if(localFlow.firstOrNull() == null) {
                emit(Resource.Empty())
            }
        }
        emitAll(localFlow.map {
            Resource.Success(data = it)
        })
    }

}


