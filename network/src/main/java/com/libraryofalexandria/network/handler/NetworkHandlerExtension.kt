package com.libraryofalexandria.network.handler

inline fun <T> Result<T>.handleNetworkErrors(handler: NetworkHandler = NetworkHandler()): Result<T> =
    this.fold(
        { value -> Result.success(value) },
        { error -> Result.failure(runCatching { handler.apply(error) }.getOrDefault(error)) }
    )