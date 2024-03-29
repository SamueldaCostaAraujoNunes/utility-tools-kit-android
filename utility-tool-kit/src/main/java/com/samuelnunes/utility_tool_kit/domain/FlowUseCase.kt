package com.samuelnunes.utility_tool_kit.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    operator fun invoke(parameters: P? = null): Flow<Resource<R>> {
        return execute(parameters)
            .catch { e -> emit(Resource.Error(e)) }
            .flowOn(coroutineDispatcher)
    }

    @Throws(RuntimeException::class)
    protected abstract fun execute(parameters: P?): Flow<Resource<R>>
}