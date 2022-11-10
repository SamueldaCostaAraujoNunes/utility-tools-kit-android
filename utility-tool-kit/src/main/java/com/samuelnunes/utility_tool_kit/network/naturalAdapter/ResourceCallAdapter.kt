package com.samuelnunes.utility_tool_kit.network.naturalAdapter

import com.samuelnunes.utility_tool_kit.domain.Resource
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ResourceCallAdapter<R>(
    private val successType: Type,
    private val annotations: Array<out Annotation>
) : CallAdapter<R, Call<Resource<R>>> {
    override fun adapt(call: Call<R>) = ResourceCall(call, annotations)
    override fun responseType(): Type = successType
}