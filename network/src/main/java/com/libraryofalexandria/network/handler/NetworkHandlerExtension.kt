package com.libraryofalexandria.network.handler

inline fun <T> Result<T>.handleNetworkErrors(): Result<T> =
    this.fold(
        { value -> Result.success(value) },
        { error -> Result.failure(runCatching { NetworkHandler().apply(error) }.getOrDefault(error)) }
    )