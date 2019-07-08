package com.libraryofalexandria.network.handler

abstract class ErrorMapper {
    abstract fun apply(throwable: Throwable): Throwable
}

suspend fun <T> handleErrors(mapper: ErrorMapper, target: suspend () -> T): T =
    try {
        target.invoke()
    } catch (incoming: Throwable) {
        throw mapper.apply(incoming)
    }