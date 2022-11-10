package com.samuelnunes.utility_tool_kit.network.FlowAdapter

import com.samuelnunes.utility_tool_kit.domain.Resource
import com.samuelnunes.utility_tool_kit.network.naturalAdapter.ResourceCall
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
) : CallAdapter<R, Flow<Resource<R>>> {
    override fun adapt(delegate: Call<R>): Flow<Resource<R>> {
        return flow {
            emit(Resource.Loading())
            emit(
                suspendCancellableCoroutine { continuation ->
                    ResourceCall(delegate, annotations).enqueue(object : Callback<Resource<R>> {
                        override fun onResponse(
                            call: Call<Resource<R>>,
                            response: Response<Resource<R>>
                        ) {
                            continuation.resume(response.body()!!)
                        }
                        override fun onFailure(call: Call<Resource<R>>, t: Throwable) {}

                    })
                    continuation.invokeOnCancellation { delegate.cancel() }
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
