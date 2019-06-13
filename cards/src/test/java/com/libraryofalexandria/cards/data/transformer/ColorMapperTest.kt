package com.libraryofalexandria.cards.data.transformer

import com.libraryofalexandria.cards.domain.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ColorMapperTest {

    private lateinit var mapper: ColorMapper

    @Before
    fun `before each`() {
        mapper = ColorMapper()
    }

    @Test
    fun `should transform raw to entity with mana cost {4}{W}{U}{B}{R}{G}`() {

        val expected = ManaCost(
            "{4}{W}{U}{B}{R}{G}",
            9.0,
            arrayListOf(
                Color.WHITE,
                Color.BLUE,
                Color.BLACK,
                Color.RED,
                Color.GREEN,
                Color.COLORLESS
            ),
            arrayListOf(
                Color.WHITE,
                Color.BLUE,
                Color.BLACK,
                Color.RED,
                Color.GREEN,
                Color.COLORLESS
            )
        )

        val response = mapper.transform("{4}{W}{U}{B}{R}{G}", 9.0)

        assertEquals(expected.toString(), response.toString())
    }

    @Test
    fun `should transform raw to entity with mana cost {0}`() {

        val expected = ManaCost(
            "{0}",
            0.0,
            arrayListOf(
                Color.COLORLESS
            ),
            arrayListOf(
                Color.COLORLESS
            )
        )

        val response = mapper.transform("{0}", 0.0)

        assertEquals(expected.toString(), response.toString())
    }

    @Test
    fun `should transform raw to entity with mana cost {}`() {

        val expected = ManaCost(
            "{}",
            0.0,
            arrayListOf(
                Color.COLORLESS
            ),
            arrayListOf()
        )

        val response = mapper.transform("{}", 0.0)

        assertEquals(expected.toString(), response.toString())
    }

    @Test
    fun `should transform raw to entity with mana cost {X}{X}{U}{G}{G}`() {

        val expected = ManaCost(
            "{X}{X}{U}{G}{G}",
            3.0,
            arrayListOf(
                Color.BLUE,
                Color.GREEN,
                Color.COLORLESS
            ),
            arrayListOf(
                Color.BLUE,
                Color.GREEN,
                Color.GREEN,
                Color.COLORLESS,
                Color.COLORLESS

            )
        )

        val response = mapper.transform("{X}{X}{U}{G}{G}", 3.0)

        assertEquals(expected.toString(), response.toString())
    }
}