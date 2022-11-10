package com.samuelnunes.utility_tool_kit.network.FlowAdapter

import com.samuelnunes.utility_tool_kit.domain.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class FlowCallAdapterFactory private constructor() : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Flow::class.java) {
            return null
        }
        val observableType: Type = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        return if(rawObservableType != Resource::class.java) {
            FlowBodyCallAdapter<Type>(observableType)
        }else {
            require(rawObservableType == Resource::class.java) { "type must be a Result" }
            require(observableType is ParameterizedType) { "resource must be parameterized" }
            val bodyType: Type = getParameterUpperBound(0, observableType)
            return FlowResultCallAdapter<Type>(bodyType, annotations)
        }
    }

    companion object {
        @JvmStatic
        fun create() = FlowCallAdapterFactory()
    }
}