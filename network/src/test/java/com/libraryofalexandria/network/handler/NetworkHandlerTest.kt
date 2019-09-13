package com.libraryofalexandria.network.handler

import com.libraryofalexandria.network.exception.NetworkError
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.InterruptedIOException
import java.net.*
import java.nio.channels.ClosedChannelException
import javax.net.ssl.SSLException

class NetworkHandlerTest {

    private val handler = NetworkHandler()

    @Test
    fun `should trigger connectivity timeout errors`() {
        val response = handler
            .apply(SocketTimeoutException())

        assertEquals(NetworkError.Connectivity.Timeout, response)
    }

    @Test
    fun `should trigger connectivity host unreachable errors`() {
        val unreachableExceptions = arrayListOf(
            BindException(),
            ClosedChannelException(),
            ConnectException(),
            NoRouteToHostException(),
            PortUnreachableException()
        )

        unreachableExceptions.forEach {
            val response = handler
                .apply(it)

            assertEquals(NetworkError.Connectivity.HostUnreachable, response)
        }
    }

    @Test
    fun `should trigger connectivity failed connection errors`() {
        val unreachableExceptions = arrayListOf(
            InterruptedIOException(),
            UnknownServiceException(),
            UnknownHostException()
        )

        unreachableExceptions.forEach {
            val response = handler
                .apply(it)

            assertEquals(NetworkError.Connectivity.FailedConnection, response)
        }
    }

    @Test
    fun `should trigger connectivity bad connection errors`() {
        val badExceptions = arrayListOf(
            ProtocolException(),
            SocketException(),
            SSLException("")
        )

        badExceptions.forEach {
            val response = handler
                .apply(it)

            assertEquals(NetworkError.Connectivity.BadConnection, response)
        }
    }

    @Test
    fun `shouldn't trigger connectivity errors`() {
        val exception = Exception()
        val response = handler
            .apply(exception)

        assertEquals(exception, response)
    }

    @Test
    fun `should trigger http unauthorized error`() {
        val response = handler
            .apply(httpException("Unauthorized", 401))

        assertEquals(NetworkError.Http.Unauthorized, response)
    }

    @Test
    fun `should trigger http not found error`() {
        val response = handler
            .apply(httpException("Not Found", 404))

        assertEquals(NetworkError.Http.NotFound, response)
    }

    @Test
    fun `should trigger http bad request error error`() {
        arrayOf(400, 403, 405, 406, 422).forEach {
            val response = handler
                .apply(httpException("Bad Request", it))

            assertEquals(NetworkError.Http.BadRequest, response)
        }
    }

    @Test
    fun `should trigger http timeout error`() {
        val response = handler
            .apply(httpException("Request Timeout", 408))

        assertEquals(NetworkError.Http.Timeout, response)
    }

    @Test
    fun `should trigger http limit rate reached error`() {
        val response = handler
            .apply(httpException("Request Rate Limiting Suppressed", 429))

        assertEquals(NetworkError.Http.LimitRateSuppressed, response)
    }

    @Test
    fun `should trigger http internal server error`() {
        val response = handler
            .apply(httpException("D'Oh", 500))

        assertEquals(NetworkError.Http.InternalServerError, response)
    }

    @Test
    fun `should trigger http generic error`() {
        val response = handler
            .apply(httpException("Never Gonna Happen", 666))

        assertEquals(NetworkError.Http.Generic, response)
    }

    private fun httpException(message: String, statusCode: Int): HttpException {
        val apiMessage = """{"code":$statusCode ,"message": "$message"}"""
        val jsonMediaType = MediaType.parse("application/json")
        val body = ResponseBody.create(jsonMediaType, apiMessage)
        return HttpException(Response.error<Any>(statusCode, body))
    }
}