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

    data class Error<out T>(val exception: Exception, val data: T? = null, val statusCode: Int? = null) : Result<T>() {
        constructor(throwable: Throwable, data: T? = null) : this(Exception(throwable), data)
        constructor(message: String, data: T? = null, statusCode: Int? = null) : this(Exception(message), data, statusCode)
        init {
            Timber.e(toString())
        }
    }

    override fun toString(): String {
        val map = when (this) {
            is Loading -> "Loading..."
            is Empty -> "Empty"
            is Success -> "Success[data=$data, statusCode=$statusCode]"
            is Error -> "Error[exception=$exception, data=$data], statusCode=$statusCode]"
        }
        return "Result: $map"
    }
}

val Result<*>.succeeded
    get() = this is Result.Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}