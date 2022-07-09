package com.samuelnunes.utility_tool_kit.domain

import com.samuelnunes.utility_tool_kit.utils.UiText
import timber.log.Timber


sealed class Result<out R> {

    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<T>(val message: UiText, val data: T? = null) : Result<T>() {
        constructor(
            throwable: Throwable,
            data: T? = null
        ) : this(
            UiText.DynamicString(
                throwable.localizedMessage ?: throwable.message ?: throwable.toString()
            ), data
        )
    }

    init {
        Timber.d(this.toString())
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[message=$message, data=$data]"
            Loading -> "Loading"
        }
    }
}

val Result<*>.succeeded
    get() = this is Result.Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}

val <T> Result<T>.data: T?
    get() = (this as? Result.Success)?.data