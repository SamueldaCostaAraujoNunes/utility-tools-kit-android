package com.samuelnunes.utility_tool_kit.domain

import timber.log.Timber


sealed class Resource<out T> {

    init {
        Timber.tag("Result")
    }

    class Loading<out T> : Resource<T>() {
        init { Timber.d(toString()) }
    }

    class Empty<out T> : Resource<T>() {
        init { Timber.d(toString()) }
    }

    data class Success<out T>(val data: T, val statusCode: Int = 200) : Resource<T>() {
        init { Timber.d(toString()) }
    }

    data class Error<out T>(
        val exception: Exception,
        val dataInCache: T? = null,
        val statusCode: Int? = null,
        val errorResponse: ErrorResponse? = null
    ) : Resource<T>() {

        abstract class ErrorResponse {}

        constructor(
            throwable: Throwable,
            dataInCache: T? = null,
            errorResponse: ErrorResponse? = null
        ) : this(
            exception = Exception(throwable),
            dataInCache = dataInCache,
            errorResponse = errorResponse
        )

        constructor(
            message: String? = null,
            dataInCache: T? = null,
            statusCode: Int? = null,
            errorResponse: ErrorResponse? = null
        ) : this(
            exception = Exception(message),
            dataInCache = dataInCache,
            statusCode = statusCode,
            errorResponse = errorResponse
        )

        init {
            Timber.e(exception)
        }
    }

    override fun toString(): String {
        return when (this) {
            is Loading -> "Loading..."
            is Empty -> "Empty"
            is Success -> "Success[data=$data, statusCode=$statusCode]"
            is Error -> "Error[exception=$exception, dataInCache=$dataInCache, statusCode=$statusCode, errorResponse=$errorResponse]"
        }
    }

    fun <D> map(mapper: (T?) -> D?): Resource<D> {
        return when (this) {
            is Loading -> Loading()
            is Empty -> Empty()
            is Success -> Success(mapper(data)!!, statusCode)
            is Error -> Error(exception, mapper(dataInCache), statusCode, errorResponse)
        }
    }
}

val Resource<*>.succeeded
    get() = this is Resource.Success && data != null

fun <T> Resource<T>.successOr(fallback: T): T {
    return (this as? Resource.Success<T>)?.data ?: fallback
}