package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.data.local.ExpansionsLocalDataSource
import com.libraryofalexandria.cards.data.network.ExpansionsRemoteDataSource
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.cards.domain.ExpansionResult
import com.libraryofalexandria.core.base.RepositoryStrategy
import com.libraryofalexandria.network.exception.NetworkError
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ExpansionRepositoryTest {

    @Mock
    private lateinit var remote: ExpansionsRemoteDataSource
    @Mock
    private lateinit var local: ExpansionsLocalDataSource

    private lateinit var repository: ExpansionRepository

    @Before
    fun `before each`() {
        MockitoAnnotations.initMocks(this)

        repository = ExpansionRepository(remote, local)
    }

    private val expansion = mock<Expansion> { Expansion::class }
    private val expansions = arrayListOf(expansion, expansion, expansion, expansion, expansion, expansion)

    @Test
    fun `should return no cached expansions`() {
        runBlocking {
            val response = ExpansionRepositoryBuilder()
                .noCache()
                .list(RepositoryStrategy.CACHE)

            when (response) {
                is ExpansionResult.Success.Cache -> {
                    Assert.assertEquals(0, response.result.count())
                    verify(local, times(1)).list()
                    verifyZeroInteractions(remote)
                }
                else -> Assert.fail("should be ExpansionResult.Success.Cache but is $response")
            }
        }
    }

    @Test
    fun `should return cached expansions`() {
        runBlocking {
            val response = ExpansionRepositoryBuilder()
                .withCache(expansions)
                .list(RepositoryStrategy.CACHE)

            when (response) {
                is ExpansionResult.Success.Cache -> {
                    Assert.assertEquals(expansions.size, response.result.count())
                    verify(local, times(1)).list()
                    verifyZeroInteractions(remote)
                }
                else -> Assert.fail("should be ExpansionResult.Success.Cache but is $response")
            }
        }
    }

    @Test
    fun `should return no cached expansions, then return and store api expansions`() {
        runBlocking {
            val response = ExpansionRepositoryBuilder()
                .withApi(expansions)
                .list(RepositoryStrategy.NETWORK)

            when (response) {
                is ExpansionResult.Success.Network -> {
                    Assert.assertEquals(expansions.size, response.result.count())
                    verify(remote, times(1)).list()
                    verify(local, times(1)).store(any())
                    verify(local, times(0)).list()
                }
                else -> Assert.fail("should be ExpansionResult.Success.Network but is $response")
            }
        }
    }

    @Test
    fun `should propagate error`() {
        runBlocking {
            val response = ExpansionRepositoryBuilder()
                .timeout()
                .list(RepositoryStrategy.NETWORK)

            when (response) {
                is ExpansionResult.Failure -> {
                    assertEquals(NetworkError.Http.Timeout, response.error)
                    verify(remote, times(1)).list()
                    verifyZeroInteractions(local)
                }
                else -> Assert.fail("should be ExpansionResult.Success.Network but is $response")
            }
        }
    }

    private inner class ExpansionRepositoryBuilder {

        fun noCache(): ExpansionRepositoryBuilder {
            whenever(local.list()).thenReturn(listOf())
            return this
        }

        fun withCache(list: List<Expansion>): ExpansionRepositoryBuilder {
            whenever(local.list()).thenReturn(list)
            return this
        }

        suspend fun withApi(list: List<Expansion>): ExpansionRepositoryBuilder {
            whenever(remote.list()).thenReturn(list)
            whenever(local.store(any())).thenReturn(list)
            return this
        }

        suspend fun list(strategy: RepositoryStrategy) = repository.list(strategy)

        suspend fun timeout(): ExpansionRepositoryBuilder {
            whenever(remote.list()).thenThrow(NetworkError.Http.Timeout)
            return this
        }
    }
}