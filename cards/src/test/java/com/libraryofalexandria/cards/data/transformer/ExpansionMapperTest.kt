package com.libraryofalexandria.cards.data.transformer

import com.libraryofalexandria.cards.data.network.entity.ExpansionResponse
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.cards.domain.Type
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ExpansionMapperTest {

    private val mapper = ExpansionMapper()

    private val expected = Expansion(
        "1",
        "INV",
        "Invasion",
        Date(),
        Type.CORE,
        200,
        ""
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
        val actual = mapper.transform(response)
        assertEquals(expected.toString(), actual.toString())
    }
}