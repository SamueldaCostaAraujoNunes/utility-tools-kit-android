package com.samuelnunes.utility_tool_kit.network

import com.google.gson.Gson
import okhttp3.ResponseBody
import kotlin.reflect.KClass


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class ErrorType(
    val kClass: KClass<out Any>,
    vararg val httpStatusCodeError: HttpStatusCode
)

fun parseError(
    statusCode: Int,
    responseBody: ResponseBody?,
    annotations: Array<out Annotation>
): Any? {
    val errorBody = responseBody?.charStream()?.readText() ?: return null

    val errorType: ErrorType = annotations.find {
        it is ErrorType && (HttpStatusCode.inStatus(
            statusCode,
            *it.httpStatusCodeError
        ) || it.httpStatusCodeError.isEmpty())
    } as? ErrorType ?: return null

    return Gson().fromJson(errorBody, errorType.kClass.java)

}