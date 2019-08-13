package com.libraryofalexandria.cards.data.local

import com.libraryofalexandria.cards.data.local.entity.Filters
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FiltersLocalDataSourceTest {

    private lateinit var dataSource: FiltersLocalDataSource

    @Before
    fun `before each`() {
        dataSource = FiltersLocalDataSource()
    }

    @Test
    fun `should return all filters`() {
        val expected = listOf(
            Filters.Expansion.CORE,
            Filters.Expansion.PROMO,
            Filters.Expansion.SUPPLEMENTAL,
            Filters.Expansion.OTHER
        )

        runBlocking {

            val result = dataSource.list()
            assertEquals(expected, result)
        }
    }
}