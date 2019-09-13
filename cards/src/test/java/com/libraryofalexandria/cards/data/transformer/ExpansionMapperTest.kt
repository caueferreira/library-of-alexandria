package com.libraryofalexandria.cards.data.transformer

import com.libraryofalexandria.cards.data.network.entity.ExpansionResponse
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.cards.domain.Filters
import com.libraryofalexandria.cards.domain.Type
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

class ExpansionMapperTest {

    @Mock
    private val filterMapper = ExpansionFilterMapper()

    private lateinit var mapper: ExpansionMapper

    @Before
    fun `before each`() {
        MockitoAnnotations.initMocks(this)

        mapper = ExpansionMapper(filterMapper)
    }

    private val expected = Expansion(
        "1",
        "INV",
        "Invasion",
        Date(),
        Type.CORE,
        200,
        "",
        Filters.Expansion.CORE
    )

    private val response = ExpansionResponse(
        "1",
        "INV",
        "Invasion",
        "",
        "CORE",
        200,
        ""
    )

    @Test
    fun `should parse correctly`() {
        whenever(filterMapper.transform(Type.CORE)).thenReturn(Filters.Expansion.CORE)

        val actual = mapper.transform(response)
        assertEquals(expected.toString(), actual.toString())
    }
}