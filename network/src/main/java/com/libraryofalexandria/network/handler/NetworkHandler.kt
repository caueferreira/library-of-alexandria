package com.libraryofalexandria.network.handler

import com.libraryofalexandria.network.exception.NetworkError
import retrofit2.HttpException
import java.net.*

class NetworkHandler : ErrorMapper() {

    override fun apply(throwable: Throwable): Throwable = when {
        throwable is HttpException -> mapNetworkErrors(throwable)
        throwable.isConnectivityException() -> mapConnectivityErrors(throwable)
        else -> throwable
    }

    private fun mapNetworkErrors(httpException: HttpException) = when (httpException.code()) {
        401 -> NetworkError.Http.Unauthorized
        400, 403, 405, 406, 422 -> NetworkError.Http.BadRequest
        404 -> NetworkError.Http.NotFound
        408 -> NetworkError.Http.Timeout
        429 -> NetworkError.Http.LimitRateSuppressed
        in 500..599 -> NetworkError.Http.InternalServerError
        else -> NetworkError.Http.Generic
    }

    private fun mapConnectivityErrors(throwable: Throwable): NetworkError.Connectivity =
        when (throwable) {
            is SocketTimeoutException -> NetworkError.Connectivity.Timeout
            is ConnectException -> NetworkError.Connectivity.HostUnreachable
            is NoRouteToHostException -> NetworkError.Connectivity.HostUnreachable
            is PortUnreachableException -> NetworkError.Connectivity.HostUnreachable
            is UnknownHostException -> NetworkError.Connectivity.HostUnreachable
            is UnknownServiceException -> NetworkError.Connectivity.FailedConnection
            is SocketException -> NetworkError.Connectivity.BadConnection
            is BindException -> NetworkError.Connectivity.BadConnection
            else -> NetworkError.Connectivity.Generic
        }
}