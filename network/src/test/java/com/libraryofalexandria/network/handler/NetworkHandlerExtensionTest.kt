package com.libraryofalexandria.network.handler

import com.libraryofalexandria.network.exception.NetworkError
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
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
