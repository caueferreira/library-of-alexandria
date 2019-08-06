package com.libraryofalexandria.cards.data.local

import com.libraryofalexandria.cache.Cache
import com.libraryofalexandria.cards.domain.Expansion
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ExpansionsLocalDataSourceTest {

    @Mock
    private lateinit var cache: Cache<Expansion>

    private lateinit var dataSource: ExpansionsLocalDataSource

    @Before
    fun `before each`() {
        MockitoAnnotations.initMocks(this)

        dataSource = ExpansionsLocalDataSource(cache)
    }

    @Test
    fun `should return empty`() {
        runBlocking {
            val response = ExpansionsLocalDataSourceBuilder()
                .empty()
                .list()

            Assert.assertEquals(0, response.count())
        }
    }

    @Test
    fun `should return cached expansions`() {
        runBlocking {
            val expansion = mock<Expansion> { Expansion::class }
            val expansions = listOf(expansion, expansion, expansion)

            val response = ExpansionsLocalDataSourceBuilder()
                .withCache(expansions)
                .list()

            Assert.assertEquals(expansions.count(), response.count())
            Assert.assertEquals(expansions, response)
        }
    }

    @Test
    fun `should return expansions when store`() {
        runBlocking {
            val expansion = mock<Expansion> { Expansion::class }
            val expansions = listOf(expansion, expansion, expansion)

            val response = ExpansionsLocalDataSourceBuilder()
                .store(expansions)

            Assert.assertEquals(expansions.count(), response.count())
            Assert.assertEquals(expansions, response)
        }
    }

    private inner class ExpansionsLocalDataSourceBuilder {

        suspend fun empty(): ExpansionsLocalDataSourceBuilder {
            whenever(cache.list()).thenReturn(arrayListOf())
            return this
        }

        suspend fun withCache(list: List<Expansion>): ExpansionsLocalDataSourceBuilder {
            whenever(cache.list()).thenReturn(list)
            return this
        }

        suspend fun store(list: List<Expansion>) = with(list) {
            whenever(cache.store(list)).thenReturn(list)
            dataSource.store(list)
        }

        suspend fun list() = dataSource.list()
    }
}