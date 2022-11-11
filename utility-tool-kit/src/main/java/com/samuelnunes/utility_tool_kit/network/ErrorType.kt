package com.samuelnunes.utility_tool_kit.network

import com.google.gson.Gson
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.Serializable
import kotlin.reflect.KClass


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class ErrorType(
    val kClass: KClass<out Serializable>,
    vararg val httpStatusCodeError: HttpStatusCode
)

fun parseError(
    statusCode: HttpStatusCode,
    responseBody: ResponseBody?,
    annotations: Array<out Annotation>
): Serializable? {
    return try {
        val errorBody = responseBody?.charStream()?.readText() ?: return null

        val errorsTypeAnnotation = annotations.filterIsInstance<ErrorType>()

        val errorType: ErrorType =
            errorsTypeAnnotation.find { annotation -> annotation.httpStatusCodeError.any { it == statusCode } } ?:
            errorsTypeAnnotation.find { annotation -> annotation.httpStatusCodeError.isEmpty() } ?:
            return null

        Gson().fromJson(errorBody, errorType.kClass.java)
    }catch (e: Exception) {
        Timber.e(e, "Não foi possivel mapear o objeto de erro")
        null
    }
}