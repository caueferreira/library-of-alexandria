package com.libraryofalexandria.cards.data.transformer

import com.libraryofalexandria.cards.data.network.CardResponse
import com.libraryofalexandria.cards.data.network.ImageUrisResponse
import com.libraryofalexandria.cards.data.network.PricesResponse
import com.libraryofalexandria.cards.data.transformer.CardMapper
import com.libraryofalexandria.cards.data.transformer.ColorMapper
import com.libraryofalexandria.cards.domain.*
import com.libraryofalexandria.cards.domain.Set
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

class CardMapperTest {

    private lateinit var mapper: CardMapper

    @Before
    fun `before each`() {
        MockitoAnnotations.initMocks(this)

        mapper = CardMapper(ColorMapper())
    }

    @Test
    fun `should transform raw to entity`() {
        val raw = CardResponse(
            "d678cf4c-60e3-40a1-a9cc-b0bd157bcf36",
            "882619f7-69b4-4ce0-be4e-1e34e608f925",
            "Chandra, Novice Pyromancer",
            "2019-07-12",
            "{3}{R}",
            4.0,
            "Legendary Planeswalker — Chandra",
            arrayListOf("R"),
            arrayListOf("R"),
            "m20",
            "Core Set 2020",
            "https://api.scryfall.com/sets/4a787360-9767-4f44-80b1-2405dc5e39c7",
            "128",
            "uncommon",
            "",
            "+1: Elementals you control get +2/+0 until end of turn.\\n-1: Add {R}{R}.\\n-2: Chandra, Novice Pyromancer deals 2 damage to any target.",
            PricesResponse(
                8.66,
                null,
                null
            ),
            "en",
            ImageUrisResponse(
                "https://img.scryfall.com/cards/small/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845",
                "https://img.scryfall.com/cards/normal/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845",
                "https://img.scryfall.com/cards/large/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845",
                "https://img.scryfall.com/cards/png/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.png?1560257845",
                "https://img.scryfall.com/cards/art_crop/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845",
                "https://img.scryfall.com/cards/border_crop/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845"
            )
        )

        val expected = Card(
            "d678cf4c-60e3-40a1-a9cc-b0bd157bcf36",
            "Chandra, Novice Pyromancer",
            "128",
            "+1: Elementals you control get +2/+0 until end of turn.\\n-1: Add {R}{R}.\\n-2: Chandra, Novice Pyromancer deals 2 damage to any target.",
            "",
            "Legendary Planeswalker — Chandra",
            Prices(
                8.66,
                null,
                null
            ),
            Rarity.UNCOMMON,
            ManaCost(
                "{3}{R}",
                4.0,
                arrayListOf(
                    Color.COLORLESS,
                    Color.RED
                ),
                arrayListOf(
                    Color.COLORLESS,
                    Color.RED
                )
            ),
            Date(),
            Images(
                "https://img.scryfall.com/cards/small/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845",
                "https://img.scryfall.com/cards/normal/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845",
                "https://img.scryfall.com/cards/large/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845",
                "https://img.scryfall.com/cards/png/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.png?1560257845",
                "https://img.scryfall.com/cards/art_crop/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845",
                "https://img.scryfall.com/cards/border_crop/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845"
            ),
            Set(
                "m20",
                "Core Set 2020",
                "https://api.scryfall.com/sets/4a787360-9767-4f44-80b1-2405dc5e39c7"
            )
        )

        val response = mapper.transform(raw)

        assertEquals(expected.toString(), response.toString())
    }
}