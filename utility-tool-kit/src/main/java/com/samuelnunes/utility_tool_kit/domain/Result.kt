package com.samuelnunes.utility_tool_kit.domain

import timber.log.Timber


sealed class Result<out T> {

    class Loading<out T> : Result<T>() {
        init {
            Timber.d(toString())
        }
    }

    class Empty<out T> : Result<T>() {
        init {
            Timber.d(toString())
        }
    }

    data class Success<out T>(val data: T, val statusCode: Int = 200) : Result<T>() {
        init {
            Timber.d(toString())
        }
    }

    data class Error<out T>(
        val exception: Exception,
        val data: T? = null,
        val statusCode: Int? = null,
        val errorResponse: ErrorResponse? = null
    ) : Result<T>() {

        open class ErrorResponse {

        }

        constructor(
            throwable: Throwable, data: T? = null, errorResponse: ErrorResponse? = null
        ) : this(
            exception = Exception(throwable),
            data = data,
            errorResponse = errorResponse
        )

        constructor(
            message: String? = null,
            data: T? = null,
            statusCode: Int? = null,
            errorResponse: ErrorResponse? = null
        ) : this(
            exception = Exception(message),
            data = data,
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
            is Error -> "Error[exception=$exception, data=$data, statusCode=$statusCode, errorResponse=$errorResponse]"
        }
    }

    fun <D> map(mapper: (T?) -> D?): Result<D> {
        return when (this) {
            is Loading -> Loading()
            is Empty -> Empty()
            is Success -> Success(mapper(data)!!, statusCode)
            is Error -> Error(exception, mapper(data), statusCode, errorResponse)
        }
    }
}

val Result<*>.succeeded
    get() = this is Result.Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}