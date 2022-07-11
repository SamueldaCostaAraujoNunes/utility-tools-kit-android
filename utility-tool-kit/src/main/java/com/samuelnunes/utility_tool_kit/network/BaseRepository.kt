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
                val statusCode = response.code()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null || statusCode == 204) {
                        Result.Empty()
                    } else {
                        Result.Success(
                            data = body,
                            statusCode = statusCode
                        )
                    }
                } else {
                    val msg = response.errorBody()?.charStream()?.readText()
                    val errorMsg = if (msg.isNullOrEmpty()) {
                        response.message()
                    } else {
                        msg
                    }
                    Result.Error(
                        message = errorMsg,
                        statusCode = statusCode
                    )
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
    ): Flow<Result<LocalType>> = flow {
        emit(Result.Loading())
        val resultFlow = query()

        val flow = if (shouldFetch(resultFlow.firstOrNull())) {
            try {
                val response = fetch()
                val statusCode = response.code()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        saveFetchResult(body)
                    }
                    resultFlow.map {
                        Result.Success(
                            data = it,
                            statusCode = statusCode
                        )
                    }
                } else {
                    val msg = response.errorBody()?.charStream()?.readText()
                    val errorMsg = if (msg.isNullOrEmpty()) {
                        response.message()
                    } else {
                        msg
                    }
                    resultFlow.map {
                        Result.Error(
                            message = errorMsg,
                            data = it,
                            statusCode = statusCode
                        )
                    }
                }
            } catch (throwable: Throwable) {
                onFetchFailed(throwable)
                resultFlow.map {
                    Result.Error(throwable)
                }
            }
        } else {
            resultFlow.map {
                Result.Success(
                    data = it
                )
            }
        }
        emitAll(flow)
    }
}


