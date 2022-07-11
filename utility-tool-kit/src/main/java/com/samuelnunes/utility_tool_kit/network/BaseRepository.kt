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
        val value: Result<RemoteType> = try {
            val response = fetch()
            val statusCode = response.code()
            if (response.isSuccessful) {
                val body = response.body()
                if (body == null || statusCode == 204) {
                    Result.Empty()
                } else {
                    Result.Success(
                        data = body as RemoteType,
                        statusCode = statusCode
                    )
                }
            } else {
                val errorBody = response.errorBody()?.charStream()?.readText()
                val errorMsg = response.message()
                Result.Error(
                    message = errorMsg,
                    statusCode = statusCode,
                    errorBody = errorBody
                )
            }
        } catch (throwable: Throwable) {
            onFetchFailed(throwable)
            Result.Error(throwable)
        }
        emit(
            value
        )
    }

    inline fun <LocalType, RemoteType> networkBoundResource(
        crossinline query: () -> Flow<LocalType>,
        crossinline fetch: suspend () -> Response<RemoteType>,
        crossinline saveFetchResult: suspend (RemoteType) -> Unit,
        crossinline onFetchFailed: (Throwable) -> Unit = { },
        crossinline shouldUpdate: (LocalType?) -> Boolean = { true }
    ): Flow<Result<LocalType>> = flow {
        emit(Result.Loading())
        val resultFlow = query()

        val firstOrNull = resultFlow.firstOrNull()

        if (firstOrNull != null) {
            resultFlow.map {
                Result.Success(
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
                            Result.Empty()
                        }
                    } else {
                        saveFetchResult(body)
                        resultFlow.map {
                            Result.Success(
                                data = it,
                                statusCode = statusCode
                            )
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.charStream()?.readText()
                    val errorMsg = response.message()
                    resultFlow.map {
                        Result.Error(
                            message = errorMsg,
                            data = it,
                            statusCode = statusCode,
                            errorBody = errorBody
                        )
                    }
                }
            } catch (throwable: Throwable) {
                onFetchFailed(throwable)
                resultFlow.map {
                    Result.Error(
                        throwable = throwable,
                        data = it
                    )
                }
            }
            emitAll(remoteFlow)
        }
    }

}


