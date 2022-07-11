package com.samuelnunes.utility_tool_kit.network

import androidx.lifecycle.LiveData
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class LiveDataCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) {
            return null
        }
        check(returnType is ParameterizedType) { "LiveData return type must be parameterized as LiveData<Foo> or LiveData<out Foo>" }
        val responseType = getParameterUpperBound(0, returnType)
        val rawLiveDataType = getRawType(responseType)
        return if (rawLiveDataType == Response::class.java) {
            check(responseType is ParameterizedType) { "Response must be parameterized as Result<Foo> or Result<out Foo>" }
            LiveDataResultCallAdapter<Any>(
                getParameterUpperBound(
                    0,
                    responseType
                )
            )
        } else {
            LiveDataBodyCallAdapter<Any>(responseType)
        }
    }

    companion object {
        @JvmStatic
        fun create() = LiveDataCallAdapterFactory()
    }
}