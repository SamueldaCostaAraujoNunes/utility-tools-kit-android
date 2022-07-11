package com.samuelnunes.domain.utils

sealed class Resource<T> {
    class Loading<T>: Resource<T>()
    class Success<T>(val data: T): Resource<T>()
    class Error<T>(val message: String, val data: T? = null) : Resource<T>() {
        constructor(throwable: Throwable, data: T? = null) : this(throwable.localizedMessage ?: throwable.message ?: throwable.toString(), data)
    }

    fun <D> convert(lmbdConverter: (T) -> D): Resource<D> {
        return when(this){
            is Loading -> Loading<D>()
            is Success -> Success(lmbdConverter(data))
            is Error -> Error(message, data?.let { lmbdConverter(it) })
        }
    }
}