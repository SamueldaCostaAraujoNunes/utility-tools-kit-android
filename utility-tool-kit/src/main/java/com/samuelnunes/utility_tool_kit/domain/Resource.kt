package com.samuelnunes.utility_tool_kit.domain

import com.samuelnunes.utility_tool_kit.network.HttpStatusCode
import timber.log.Timber
import java.io.Serializable


sealed class Resource<out T> : Serializable {

    companion object {
        fun <T> emptyOrSuccess(data: T?, statusCode: HttpStatusCode = HttpStatusCode.OK): Resource<T> =
            if (data == null || (data is Collection<*> && data.isEmpty()) || statusCode == HttpStatusCode.NO_CONTENT) {
                Empty()
            } else {
                Success(data, statusCode)
            }
    }

    init {
        Timber.tag("Result")
    }

    class Loading<out T> : Resource<T>() {
        init { Timber.d(toString()) }
    }

    class Empty<out T> : Resource<T>() {
        init { Timber.d(toString()) }
    }

    data class Success<out T>(val data: T, val statusCode: HttpStatusCode = HttpStatusCode.OK) : Resource<T>() {
        init { Timber.d(toString()) }
    }

    data class Error<out T>(
        val exception: Exception,
        val dataInCache: T? = null,
        val statusCode: HttpStatusCode? = null,
        val errorData: Serializable? = null
    ) : Resource<T>() {

        constructor(
            throwable: Throwable,
            dataInCache: T? = null,
            errorData: Serializable? = null
        ) : this(
            exception = Exception(throwable),
            dataInCache = dataInCache,
            errorData = errorData
        )

        constructor(
            message: String? = null,
            dataInCache: T? = null,
            statusCode: HttpStatusCode? = null,
            errorData: Serializable? = null
        ) : this(
            exception = Exception(message),
            dataInCache = dataInCache,
            statusCode = statusCode,
            errorData = errorData
        )

        init {
            Timber.d(toString())
        }
    }

    override fun toString(): String {
        return when (this) {
            is Loading -> "Loading..."
            is Empty -> "Empty"
            is Success -> "Success[data=$data, statusCode=$statusCode]"
            is Error<T> -> "Error[exception=$exception, dataInCache=$dataInCache, statusCode=$statusCode, errorResponse=$errorData]"
        }
    }

    fun <D> map(
        mapperSuccess: (T?) -> D? = { null },
        mapperError: (Serializable?) -> Serializable? = { it }
    ): Resource<D> {
        return when (this) {
            is Loading -> Loading()
            is Empty -> Empty()
            is Success -> Success(mapperSuccess(data)!!, statusCode)
            is Error -> Error(
                exception,
                mapperSuccess(dataInCache),
                statusCode,
                mapperError(errorData)
            )
        }
    }
}

val Resource<*>.succeeded
    get() = this is Resource.Success && data != null

fun <T> Resource<T>.successOr(fallback: T): T {
    return (this as? Resource.Success<T>)?.data ?: fallback
}