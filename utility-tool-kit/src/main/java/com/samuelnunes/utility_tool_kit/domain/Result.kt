package com.samuelnunes.utility_tool_kit.domain

import com.bumptech.glide.load.engine.Resource
import retrofit2.Response
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
        val errorBody: String? = null,

        ) : Result<T>() {
        constructor(throwable: Throwable, data: T? = null, errorBody: String? = null) : this(
            exception = Exception(throwable),
            data = data,
            errorBody = errorBody
        )

        constructor(
            message: String,
            data: T? = null,
            errorBody: String? = null,
            statusCode: Int? = null
        ) : this(Exception(message), data, statusCode, errorBody)

        init {
            Timber.e(exception)
        }
    }

    override fun toString(): String {
        return when (this) {
            is Loading -> "Loading..."
            is Empty -> "Empty"
            is Success -> "Success[data=$data, statusCode=$statusCode]"
            is Error -> "Error[exception=$exception, data=$data, statusCode=$statusCode, errorBody=$errorBody]"
        }
    }

    fun <D> map(mapper: (T?) -> D?): Result<D> {
        return when(this){
            is Loading -> Loading()
            is Empty -> Empty()
            is Success -> Success(mapper(data)!!, statusCode)
            is Error -> Error(exception, mapper(data), statusCode, errorBody)
        }
    }
}

val Result<*>.succeeded
    get() = this is Result.Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}