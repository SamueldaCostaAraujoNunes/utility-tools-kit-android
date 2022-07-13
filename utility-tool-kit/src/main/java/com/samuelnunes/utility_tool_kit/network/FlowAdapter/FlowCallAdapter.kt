package com.samuelnunes.utility_tool_kit.network.FlowAdapter

import com.samuelnunes.utility_tool_kit.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FlowResultCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, Flow<Result<T>>> {
    override fun adapt(call: Call<T>): Flow<Result<T>> {
        return flow {
            emit(
                suspendCancellableCoroutine { continuation ->
                    call.clone().enqueue(object : Callback<T> {

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            val statusCode = response.code()
                            continuation.resume(
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
                                    val errorBody = response.errorBody()?.charStream()?.readText()
                                    val errorMsg = response.message()
                                    Result.Error(
                                        message = errorMsg,
                                        statusCode = statusCode,
                                        errorBody = errorBody
                                    )
                                }
                            )
                        }

                        override fun onFailure(call: Call<T>, throwable: Throwable) {
                            continuation.resume(Result.Error(throwable))
                        }
                    })
                    continuation.invokeOnCancellation { call.cancel() }
                }
            )
        }
    }

    override fun responseType() = responseType
}


class FlowBodyCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Flow<T>> {
    override fun adapt(call: Call<T>): Flow<T> {
        return flow {
            emit(
                suspendCancellableCoroutine { continuation ->
                    call.clone().enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            try {
                                continuation.resume(response.body()!!)
                            } catch (e: Exception) {
                                continuation.resumeWithException(e)
                            }
                        }
                    })
                    continuation.invokeOnCancellation { call.cancel() }
                }
            )
        }
    }

    override fun responseType() = responseType
}
