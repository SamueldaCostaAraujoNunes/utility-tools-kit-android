package com.samuelnunes.utility_tool_kit.network.FlowAdapter

import com.samuelnunes.utility_tool_kit.R
import com.samuelnunes.utility_tool_kit.domain.Result
import com.samuelnunes.utility_tool_kit.network.parseError
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

class FlowResultCallAdapter<R>(
    private val responseType: Type,
    private val annotations: Array<out Annotation>
) : CallAdapter<R, Flow<Result<R>>> {
    override fun adapt(call: Call<R>): Flow<Result<R>> {
        return flow {
            emit(
                suspendCancellableCoroutine { continuation ->
                    call.clone().enqueue(object : Callback<R> {

                        override fun onResponse(call: Call<R>, response: Response<R>) {
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
                                    val errorResponse = parseError(
                                        statusCode = statusCode,
                                        responseBody = response.errorBody(),
                                        annotations = annotations
                                    )
                                    Result.Error(
                                        message = response.message(),
                                        statusCode = statusCode,
                                        errorResponse = errorResponse
                                    )
                                }
                            )
                        }

                        override fun onFailure(call: Call<R>, throwable: Throwable) {
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
