package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.data.local.FiltersLocalDataSource
import com.libraryofalexandria.cards.domain.Filters
import com.libraryofalexandria.cards.view.expansions.transformers.ExpansionFilterViewEntityMapper
import com.libraryofalexandria.cards.view.expansions.ui.FilterViewEntity
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class FiltersRepositoryTest {

    @Mock
    private lateinit var local: FiltersLocalDataSource

    private lateinit var repository: FiltersRepository

    @Before
    fun `before each`() {
        MockitoAnnotations.initMocks(this)

        repository = FiltersRepository(local)
    }

    private val filter = mock<Filters.Expansion> { Filters.Expansion::class }
    private val filters = arrayListOf(filter, filter)

    @Test
    fun `should return no cached filters`() {
        runBlocking {
            val response = FiltersRepositoryBuilder()
                .noCache()
                .list()

            Assert.assertEquals(0, response.count())
            verify(local, times(1)).list()
        }
    }

    @Test
    fun `should return cached filters`() {
        runBlocking {
            val response = FiltersRepositoryBuilder()
                .withCache(filters)
                .list()

            Assert.assertEquals(filters.size, response.count())
            verify(local, times(1)).list()
        }
    }


    private inner class FiltersRepositoryBuilder {

        suspend fun noCache(): FiltersRepositoryBuilder {
            whenever(local.list()).thenReturn(listOf())
            return this
        }

        suspend fun withCache(list: List<Filters.Expansion>): FiltersRepositoryBuilder {
            whenever(local.list()).thenReturn(list)
            return this
        }

        suspend fun list() = repository.list()
    }
}