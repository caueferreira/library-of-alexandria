package com.libraryofalexandria.network.handler

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test

class NetworkHandlerExtensionTest {

    private val mapper = NetworkHandler()

    @Test
    fun `should trigger mapper apply on error`() {
        try {
            runBlocking {
                handleErrors(mapper) {
                    Exception()
                }
            }
        } catch (e: Exception) {
            verify(mapper.apply(any()),times(1))
        }
    }
}