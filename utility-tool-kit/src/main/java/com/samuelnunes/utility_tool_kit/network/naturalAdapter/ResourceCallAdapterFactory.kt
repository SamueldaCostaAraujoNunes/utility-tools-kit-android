package com.samuelnunes.utility_tool_kit.network.naturalAdapter

import com.samuelnunes.utility_tool_kit.domain.Resource
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResourceCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType) { "Return type must be a parameterized type." }

        val resourceType = getParameterUpperBound(0, returnType)
        if (getRawType(resourceType) != Resource::class.java) return null
        check(resourceType is ParameterizedType) { "Response type must be a parameterized type." }

        val responseType = getParameterUpperBound(0, resourceType)
        return ResourceCallAdapter<Any>(responseType, annotations)
    }
    companion object {
        @JvmStatic
        fun create() = ResourceCallAdapterFactory()
    }
}
