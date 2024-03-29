package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.cards.data.FiltersRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class FetchFiltersTest {

    @Mock
    private lateinit var repository: FiltersRepository

    private lateinit var useCase: FetchFilters

    private val filter = mock<Filters.Expansion> { Filters.Expansion::class }
    private val filters = arrayListOf(filter, filter, filter)

    @Before
    fun `before each`() {
        MockitoAnnotations.initMocks(this)

        useCase = FetchFilters(repository)
    }

    @Test
    fun `should return cache`() {
        runBlocking {

            val responses = FetchFiltersBuilder()
                .withCache(filters)
                .fetch()
                .toList()

            Assert.assertEquals(filters.size, responses.first().size)

            verify(repository, times(1)).list()
        }
    }

    private inner class FetchFiltersBuilder {
        suspend fun withCache(list: List<Filters.Expansion>): FetchFiltersBuilder {
            whenever(repository.list()).thenReturn(list)
            return this
        }

        suspend fun fetch() = useCase.fetch()
    }
}