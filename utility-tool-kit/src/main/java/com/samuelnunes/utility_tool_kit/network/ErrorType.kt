package com.samuelnunes.utility_tool_kit.network

import com.google.gson.Gson
import com.samuelnunes.utility_tool_kit.domain.HttpStatusCodeError
import com.samuelnunes.utility_tool_kit.domain.Resource
import okhttp3.ResponseBody
import kotlin.reflect.KClass


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class ErrorType(val httpStatusCodeError: HttpStatusCodeError, val kClass: KClass<out Resource.Error.ErrorResponse>)

fun parseError(statusCode: Int, responseBody: ResponseBody?, annotations: Array<out Annotation>): Resource.Error.ErrorResponse? {
    val errorBody = responseBody?.charStream()?.readText() ?: return null

    val errorType: ErrorType = annotations.find {
        it is ErrorType && it.httpStatusCodeError.statusCode == statusCode
    } as? ErrorType ?: return null

    return Gson().fromJson(errorBody, errorType.kClass.java)

}