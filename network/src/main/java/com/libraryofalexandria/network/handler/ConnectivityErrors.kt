package com.libraryofalexandria.network.handler

import java.io.InterruptedIOException
import java.net.*
import java.nio.channels.ClosedChannelException
import javax.net.ssl.SSLException

fun Throwable.isConnectivityException(): Boolean =
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