package com.samuelnunes.utility_tool_kit.network

import androidx.lifecycle.LiveData
import com.samuelnunes.utility_tool_kit.domain.Result
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean


class LiveDataCallAdapter<R>(private val responseType: Type) : CallAdapter<R, LiveData<Result<R>>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): LiveData<Result<R>> {
        return object : LiveData<Result<R>>(Result.Loading()) {
            var started = AtomicBoolean(false)

            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<R> {
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
                                val errorBody = response.errorBody()?.string()
                                val errorMsg = response.message()
                                Result.Error<R>(
                                    message = errorMsg,
                                    statusCode = statusCode,
                                    errorBody = errorBody
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