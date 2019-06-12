package com.libraryofalexandria.network.exception

sealed class NetworkError : Throwable() {

    sealed class Http : NetworkError() {
        object Unauthorized : Http()
        object NotFound : Http()
        object Timeout : Http()
        object LimitRateSuppressed : Http()
        object InternalServerError : Http()
        object BadRequest : Http()
        object Generic : Http()
    }

    sealed class Connectivity : NetworkError() {
        object Timeout : Connectivity()
        object HostUnreachable : Connectivity()
        object FailedConnection : Connectivity()
        object BadConnection : Connectivity()
        object Generic : Connectivity()
    }
}