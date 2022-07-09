package com.samuelnunes.utility_tool_kit.domain

import com.samuelnunes.utility_tool_kit.utils.UiText
import timber.log.Timber


sealed class Result<out T> {

    class Loading<out T> : Result<T>()
    class Empty<out T> : Result<T>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val message: UiText, val data: T? = null) : Result<T>() {
        constructor(message: String, data: T? = null) : this(
            UiText.DynamicString(message), data
        )
        constructor(
            throwable: Throwable,
            data: T? = null
        ) : this(
            throwable.localizedMessage ?: throwable.message ?: throwable.toString(), data
        ) {
            Timber.e(throwable)
        }
    }

    init {
        Timber.d(this.toString())
    }

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Empty -> "Empty"
            is Error -> "Error[message=$message, data=$data]"
            is Loading -> "Loading"
        }
    }
}

val Result<*>.succeeded
    get() = this is Result.Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}