package com.caueferreira.network.handler

import com.caueferreira.network.exception.NetworkError
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.*
import java.nio.channels.ClosedChannelException
import javax.net.ssl.SSLException


class NetworkHandler(private val error: Throwable) {
    private val mappedError: Throwable

    init {
        mappedError = when {
            error is HttpException -> mapNetworkErrors(error)
            error.isConnectivityException() -> mapConnectivityErrors(error)
            else -> error
        }
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
            is BindException -> NetworkError.Connectivity.HostUnreachable
            is ClosedChannelException -> NetworkError.Connectivity.HostUnreachable
            is ConnectException -> NetworkError.Connectivity.HostUnreachable
            is NoRouteToHostException -> NetworkError.Connectivity.HostUnreachable
            is PortUnreachableException -> NetworkError.Connectivity.HostUnreachable
            is InterruptedIOException -> NetworkError.Connectivity.FailedConnection
            is UnknownServiceException -> NetworkError.Connectivity.FailedConnection
            is UnknownHostException -> NetworkError.Connectivity.FailedConnection
            is ProtocolException -> NetworkError.Connectivity.BadConnection
            is SocketException -> NetworkError.Connectivity.BadConnection
            is SSLException -> NetworkError.Connectivity.BadConnection
            else -> NetworkError.Connectivity.Generic
        }

    private fun Throwable.isConnectivityException(): Boolean =
        this is BindException ||
                this is ClosedChannelException ||
                this is ConnectException ||
                this is InterruptedIOException ||
                this is NoRouteToHostException ||
                this is PortUnreachableException ||
                this is ProtocolException ||
                this is SocketException ||
                this is SocketTimeoutException ||
                this is SSLException ||
                this is UnknownHostException ||
                this is UnknownServiceException
}