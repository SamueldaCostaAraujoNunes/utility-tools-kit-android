package com.samuelnunes.utility_tool_kit.network.LiveDataAdapter

import androidx.lifecycle.LiveData
import com.samuelnunes.utility_tool_kit.domain.Result
import com.samuelnunes.utility_tool_kit.network.parseError
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataBodyCallAdapter<R>(
    private val responseType: Type
) :
    CallAdapter<R, LiveData<R>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): LiveData<R> {
        return object : LiveData<R>() {
            var started = AtomicBoolean(false)

            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.clone().enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            if (response.isSuccessful && response.code() != 204) {
                                postValue(response.body())
                            } else {
                                postValue(null)
                            }
                        }

                        override fun onFailure(call: Call<R>, throwable: Throwable) {
                            Timber.wtf(throwable)
                            postValue(null)
                        }
                    })
                }
            }
        }
    }
}

class LiveDataResultCallAdapter<R>(
    private val responseType: Type,
    private val annotations: Array<out Annotation>
) : CallAdapter<R, LiveData<Result<R>>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): LiveData<Result<R>> {
        return object : LiveData<Result<R>>(Result.Loading()) {
            var started = AtomicBoolean(false)

            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.clone().enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            val statusCode = response.code()
                            if (response.isSuccessful) {
                                val body = response.body()
                                if (body == null || statusCode == 204) {
                                    postValue(Result.Empty())
                                } else {
                                    postValue(
                                        Result.Success(
                                            data = body,
                                            statusCode = statusCode
                                        )
                                    )
                                }
                            } else {
                                val errorResponse = parseError(
                                    statusCode = statusCode,
                                    responseBody = response.errorBody(),
                                    annotations = annotations
                                )
                                Result.Error<R>(
                                    message = response.message(),
                                    statusCode = statusCode,
                                    errorResponse = errorResponse
                                )
                            }
                        }

                        override fun onFailure(call: Call<R>, throwable: Throwable) {
                            postValue(Result.Error(throwable))
                        }
                    })
                }
            }
        }
    }
}