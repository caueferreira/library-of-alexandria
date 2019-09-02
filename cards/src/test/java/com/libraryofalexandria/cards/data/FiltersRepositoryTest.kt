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
    @Mock
    private lateinit var mapper: ExpansionFilterViewEntityMapper

    private lateinit var repository: FiltersRepository

    @Before
    fun `before each`() {
        MockitoAnnotations.initMocks(this)

        repository = FiltersRepository(local, mapper)
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
            verifyZeroInteractions(mapper)
        }
    }

    @Test
    fun `should return cached filters`() {
        runBlocking {
            val response = FiltersRepositoryBuilder()
                .withCache(filters)
                .list()

            Assert.assertEquals(filters.size, response.count())
            verify(mapper, times(filters.size)).transform(any())
            verify(local, times(1)).list()
        }
    }


    private inner class FiltersRepositoryBuilder {

        suspend fun noCache(): FiltersRepositoryBuilder {
            whenever(local.list()).thenReturn(listOf())
            return this
        }

        suspend fun withCache(list: List<Filters.Expansion>): FiltersRepositoryBuilder {
            whenever(mapper.transform(any())).thenReturn(mock { FilterViewEntity::class })
            whenever(local.list()).thenReturn(list)
            return this
        }

        suspend fun list() = repository.list()
    }
}