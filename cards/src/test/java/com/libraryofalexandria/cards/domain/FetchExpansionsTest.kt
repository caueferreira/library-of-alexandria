package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.cards.data.ExpansionRepository
import com.libraryofalexandria.core.base.RepositoryStrategy
import com.libraryofalexandria.network.exception.NetworkError
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class FetchExpansionsTest {

    @Mock
    private lateinit var repository: ExpansionRepository

    private lateinit var useCase: FetchExpansions

    @Before
    fun `before each`() {
        MockitoAnnotations.initMocks(this)

        useCase = FetchExpansions(repository)
    }

    private val expansion = mock<Expansion> { Expansion::class }
    private val expansions = arrayListOf(expansion, expansion, expansion, expansion, expansion, expansion)

    private val cache = ExpansionResult.Success.Cache(arrayListOf())
    private val network = ExpansionResult.Success.Network(expansions)
    private val failure = ExpansionResult.Failure(NetworkError.Http.BadRequest)

    @Test
    fun `should return cache and network`() {
        runBlocking {

            val responses = FetchExpansionsBuilder()
                .withCache(cache)
                .withNetwork(network)
                .fetch()
                .toList()

            assertEquals(2, responses.size)
            val firstResponse = responses.first()
            val secondResponse = responses.second()

            assert(firstResponse is ExpansionResult.Success.Cache)

            when (firstResponse) {
                is ExpansionResult.Success.Cache -> assertEquals(0, firstResponse.result.size)
                else -> fail("First Expansion result should be cache")
            }

            assert(secondResponse is ExpansionResult.Success.Network)
            when (secondResponse) {
                is ExpansionResult.Success.Network -> assertEquals(expansions.size, secondResponse.result.size)
                else -> fail("Second Expansion result should be network")
            }

            verify(repository, times(1)).list(RepositoryStrategy.CACHE)
            verify(repository, times(1)).list(RepositoryStrategy.NETWORK)
            verify(repository, times(1)).store(any())
        }
    }

    @Test
    fun `should propagate error`() {
        runBlocking {

            val responses = FetchExpansionsBuilder()
                .withCache(cache)
                .withError(failure)
                .fetch()
                .toList()

            assertEquals(2, responses.size)
            val firstResponse = responses.first()
            val secondResponse = responses.second()

            assert(firstResponse is ExpansionResult.Success.Cache)

            when (firstResponse) {
                is ExpansionResult.Success.Cache -> assertEquals(0, firstResponse.result.size)
                else -> fail("First Expansion result should be cache")
            }

            assert(secondResponse is ExpansionResult.Failure)
            when (secondResponse) {
                is ExpansionResult.Failure -> assert(secondResponse.error is NetworkError.Http.BadRequest)
                else -> fail("Second Expansion result should be error")
            }

            verify(repository, times(1)).list(RepositoryStrategy.CACHE)
            verify(repository, times(1)).list(RepositoryStrategy.NETWORK)
            verify(repository, times(0)).store(any())
        }
    }

    private inner class FetchExpansionsBuilder {
        suspend fun withCache(result: ExpansionResult.Success.Cache): FetchExpansionsBuilder {
            whenever(repository.list(RepositoryStrategy.CACHE)).thenReturn(result)
            return this
        }

        suspend fun withNetwork(result: ExpansionResult.Success.Network): FetchExpansionsBuilder {
            whenever(repository.list(RepositoryStrategy.NETWORK)).thenReturn(result)
            whenever(repository.store(result)).thenReturn(result)
            return this
        }

        suspend fun withError(result: ExpansionResult.Failure): FetchExpansionsBuilder {
            whenever(repository.list(RepositoryStrategy.NETWORK)).thenReturn(result)
            return this
        }

        suspend fun fetch() = useCase.fetch()
    }
}

private fun <E> List<E>.second(): E = this[1]