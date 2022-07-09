package com.samuelnunes.utility_tool_kit.network

import com.samuelnunes.utility_tool_kit.domain.Result
import kotlinx.coroutines.flow.*
import retrofit2.Response


abstract class BaseRepository {

    inline fun <RemoteType> networkBoundResource(
        crossinline fetch: suspend () -> Response<RemoteType>,
        crossinline onFetchFailed: (Throwable) -> Unit = { }
    ): Flow<Result<RemoteType>> = flow {
        emit(Result.Loading())
        emit(
            try {
                val response = fetch()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null || response.code() == 204) {
                        Result.Empty()
                    } else {
                        Result.Success(body)
                    }
                } else {
                    val msg = response.errorBody()?.string()
                    val errorMsg = if (msg.isNullOrEmpty()) {
                        response.message()
                    } else {
                        msg
                    }
                    Result.Error(message = errorMsg)
                }
            } catch (throwable: Throwable) {
                onFetchFailed(throwable)
                Result.Error(throwable)
            }
        )
    }

    inline fun <LocalType, RemoteType> networkBoundResource(
        crossinline query: () -> Flow<LocalType>,
        crossinline fetch: suspend () -> Response<RemoteType>,
        crossinline saveFetchResult: suspend (RemoteType) -> Unit,
        crossinline onFetchFailed: (Throwable) -> Unit = { },
        crossinline shouldFetch: (LocalType?) -> Boolean = { true }
    ) = flow {
        emit(Result.Loading<RemoteType>())
        val resultFlow = query()

        val flow = if (shouldFetch(resultFlow.firstOrNull())) {
            try {
                val response = fetch()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null || response.code() == 204) {
                        resultFlow
                    } else {
                        saveFetchResult(body)
                        resultFlow.map { Result.Success(it) }
                    }
                } else {
                    val msg = response.errorBody()?.string()
                    val errorMsg = if (msg.isNullOrEmpty()) {
                        response.message()
                    } else {
                        msg
                    }
                    resultFlow.map { Result.Error(errorMsg, it) }
                }
            } catch (throwable: Throwable) {
                onFetchFailed(throwable)
                resultFlow.map { Result.Error(throwable, it) }
            }
        } else {
            resultFlow.map { Result.Success(it) }
        }
        emitAll(flow)
    }
}


