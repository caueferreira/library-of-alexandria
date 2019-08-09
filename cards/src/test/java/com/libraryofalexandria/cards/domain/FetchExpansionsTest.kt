package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.cards.data.ExpansionRepository
import com.libraryofalexandria.core.base.RepositoryStrategy
import com.libraryofalexandria.network.exception.NetworkError
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.flow.catch
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

    private val cache = arrayListOf<Expansion>()
    private val network = expansions
    private val failure = NetworkError.Http.BadRequest

    @Test
    fun `should return cache and network`() {
        runBlocking {

            val responses = FetchExpansionsBuilder()
                .withCache(cache)
                .withNetwork(network)
                .fetch()
                .toList()

            assertEquals(2, responses.size)
            assertEquals(0, responses.first().size)
            assertEquals(expansions.size, responses.second().size)

            verify(repository, times(1)).list(RepositoryStrategy.CACHE)
            verify(repository, times(1)).list(RepositoryStrategy.NETWORK)
            verify(repository, times(1)).store(any())
        }
    }

    @Test(expected = NetworkError.Http.BadRequest::class)
    fun `should propagate error`() {
        runBlocking {

            val responses = FetchExpansionsBuilder()
                .withCache(cache)
                .withError(failure)
                .fetch()
                .toList()

            assertEquals(12, responses.size)
            assertEquals(0, responses.first().size)

            verify(repository, times(1)).list(RepositoryStrategy.CACHE)
            verify(repository, times(1)).list(RepositoryStrategy.NETWORK)
            verify(repository, times(0)).store(any())
        }
    }

    private inner class FetchExpansionsBuilder {
        suspend fun withCache(list: List<Expansion>): FetchExpansionsBuilder {
            whenever(repository.list(RepositoryStrategy.CACHE)).thenReturn(list)
            return this
        }

        suspend fun withNetwork(list: List<Expansion>): FetchExpansionsBuilder {
            whenever(repository.list(RepositoryStrategy.NETWORK)).thenReturn(list)
            whenever(repository.store(list)).thenReturn(list)
            return this
        }

        suspend fun withError(error: NetworkError): FetchExpansionsBuilder {
            whenever(repository.list(RepositoryStrategy.NETWORK)).thenThrow(error)
            return this
        }

        suspend fun fetch() = useCase.fetch()
    }
}

private fun <E> List<E>.second(): E = this[1]