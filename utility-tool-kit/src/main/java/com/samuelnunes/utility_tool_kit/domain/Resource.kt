package com.samuelnunes.utility_tool_kit.domain

import com.samuelnunes.utility_tool_kit.network.HttpStatusCode
import timber.log.Timber
import java.io.Serializable


sealed class Resource<out T> : Serializable {

    companion object {
        fun <T> emptyOrSuccess(
            data: T?,
            statusCode: HttpStatusCode = HttpStatusCode.OK
        ): Resource<T> =
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
        init {
            Timber.d(toString())
        }

        fun <D> map() = Loading<D>()
    }

    class Empty<out T> : Resource<T>() {
        init {
            Timber.d(toString())
        }

        fun <D> map() = Empty<D>()
    }

    data class Success<out T>(val data: T, val statusCode: HttpStatusCode = HttpStatusCode.OK) :
        Resource<T>() {
        init {
            Timber.d(toString())
        }

        fun <D> map(mapper: (T) -> D) = Success(mapper(data), statusCode)
    }

    data class Error<out T>(
        val exception: Exception,
        val statusCode: HttpStatusCode? = null,
        val errorData: Serializable? = null
    ) : Resource<T>() {

        constructor(
            throwable: Throwable,
            errorData: Serializable? = null
        ) : this(
            exception = Exception(throwable),
            errorData = errorData
        )

        constructor(
            message: String? = null,
            statusCode: HttpStatusCode? = null,
            errorData: Serializable? = null
        ) : this(
            exception = Exception(message),
            statusCode = statusCode,
            errorData = errorData
        )

        init {
            Timber.d(toString())
        }

        fun <D> map(mapperError: (Serializable?) -> Serializable? = { it }) = Error<D>(
            exception,
            statusCode,
            mapperError(errorData)
        )
    }

    override fun toString(): String {
        return when (this) {
            is Loading -> "Loading..."
            is Empty -> "Empty"
            is Success -> "Success[data=$data, statusCode=$statusCode]"
            is Error -> "Error[exception=$exception, statusCode=$statusCode, errorResponse=$errorData]"
        }
    }

    fun <D> map(
        mapperError: (Serializable?) -> Serializable? = { it },
        mapperSuccess: ((T) -> D)
    ): Resource<D> {
        return when (this) {
            is Loading -> map()
            is Empty -> map()
            is Success -> map(mapperSuccess)
            is Error -> map(mapperError)
        }
    }
}

fun <T> Resource<T>.successOr(fallback: T): T {
    return (this as? Resource.Success<T>)?.data ?: fallback
}