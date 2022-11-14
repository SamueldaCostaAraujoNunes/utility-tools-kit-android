package com.samuelnunes.utility_tool_kit.network

import com.samuelnunes.utility_tool_kit.domain.Resource
import kotlinx.coroutines.flow.*
import retrofit2.Response


abstract class BaseRepository {

    inline fun <LocalType, RemoteType> networkBoundResource(
        crossinline query: () -> Flow<LocalType>,
        crossinline fetch: suspend () -> Resource<RemoteType>,
        crossinline saveFetchResult: suspend (LocalType) -> Unit,
        crossinline convertRemoteToLocal: (RemoteType) -> LocalType,
        crossinline onFetchFailed: (Throwable) -> Unit = { },
        crossinline shouldUpdate: (LocalType?) -> Boolean = { true }
    ): Flow<Resource<LocalType>> = flow {
        emit(Resource.Loading())
        val localFlow = query()

        val firstOrNull = localFlow.firstOrNull()

        if (shouldUpdate(firstOrNull)) {
            Resource.emptyOrSuccess(data = firstOrNull).also {
                if (it is Resource.Success) {
                    emit(it)
                }
            }
            when (val resultFetch = fetch()) {
                is Resource.Success<RemoteType> -> saveFetchResult(convertRemoteToLocal(resultFetch.data))
                is Resource.Error -> {
                    onFetchFailed(resultFetch.exception)
                    emit(resultFetch.map { convertRemoteToLocal(it) })
                }
                else -> {}
            }
        }
        emitAll(localFlow.map {
            Resource.emptyOrSuccess(data = it)
        })
    }

}


