package com.samuelnunes.utility_tool_kit.network

import androidx.lifecycle.LiveData
import com.samuelnunes.utility_tool_kit.domain.Result
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType

import java.lang.reflect.Type;


class LiveDataCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) {
            return null
        }
        val observableType: Type = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        require(rawObservableType == Result::class.java) { "type must be a Result" }
        require(observableType is ParameterizedType) { "resource must be parameterized" }
        val bodyType: Type = getParameterUpperBound(0, observableType)
        return LiveDataCallAdapter<Type>(bodyType)
    }
}