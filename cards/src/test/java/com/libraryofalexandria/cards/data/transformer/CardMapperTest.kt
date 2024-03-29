package com.libraryofalexandria.cards.data.transformer

import com.libraryofalexandria.cards.data.network.entity.CardResponse
import com.libraryofalexandria.cards.data.network.entity.ImageUrisResponse
import com.libraryofalexandria.cards.data.network.entity.PricesResponse
import com.libraryofalexandria.cards.domain.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class CardMapperTest {

    private lateinit var mapper: CardMapper

    @Before
    fun `before each`() {
        mapper = CardMapper(ColorMapper())
    }

    @Test
    fun `should transform raw to entity`() {
        val raw = CardResponse(
            "d678cf4c-60e3-40a1-a9cc-b0bd157bcf36",
            "882619f7-69b4-4ce0-be4e-1e34e608f925",
            "Chandra, Novice Pyromancer",
            "{3}{R}",
            4.0,
            "Legendary Planeswalker — Chandra",
            arrayListOf("R"),
            arrayListOf("R"),
            "m20",
            "Core ExpansionState 2020",
            "https://api.scryfall.com/expansions/4a787360-9767-4f44-80b1-2405dc5e39c7",
            "128",
            "uncommon",
            "",
            "+1: Elementals you control list +2/+0 until end of turn.\\n-1: Add {R}{R}.\\n-2: Chandra, Novice Pyromancer deals 2 damage to any target.",
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
            "+1: Elementals you control list +2/+0 until end of turn.\\n-1: Add {R}{R}.\\n-2: Chandra, Novice Pyromancer deals 2 damage to any target.",
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
                    Color.RED,
                    Color.COLORLESS
                ),
                arrayListOf(
                    Color.RED,
                    Color.COLORLESS

                )
            ),
            Images(
                "https://img.scryfall.com/cards/small/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845",
                "https://img.scryfall.com/cards/normal/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845",
                "https://img.scryfall.com/cards/large/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845",
                "https://img.scryfall.com/cards/png/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.png?1560257845",
                "https://img.scryfall.com/cards/art_crop/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845",
                "https://img.scryfall.com/cards/border_crop/front/d/6/d678cf4c-60e3-40a1-a9cc-b0bd157bcf36.jpg?1560257845"
            ),
            CardExpansion(
                "m20",
                "Core ExpansionState 2020",
                "https://api.scryfall.com/expansions/4a787360-9767-4f44-80b1-2405dc5e39c7"
            )
        )

        val response = mapper.transform(raw)

        assertEquals(expected.toString(), response.toString())
    }
}