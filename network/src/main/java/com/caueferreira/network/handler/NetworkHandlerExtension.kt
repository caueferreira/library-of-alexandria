package com.caueferreira.network.handler

inline fun <T> Result<T>.flatMapError(transformError: (Throwable) -> Throwable): Result<T> =
    this.fold(
        { value -> Result.success(value) },
        { error -> Result.failure(runCatching { transformError(error) }.getOrDefault(error)) }
    )
