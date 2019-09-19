package com.libraryofalexandria.network.handler

import com.libraryofalexandria.network.exception.NetworkError
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.net.SocketTimeoutException

class NetworkHandlerExtensionTest {

    private val mapper = NetworkHandler()

    @Test(expected = NetworkError.Connectivity.Timeout::class)
    fun `should trigger mapper apply and map to timeout error`() {
        runBlocking {
            handleErrors(mapper) {
                throw SocketTimeoutException()
            }
        }
    }
}
