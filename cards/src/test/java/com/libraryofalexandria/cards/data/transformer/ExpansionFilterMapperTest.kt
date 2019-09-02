package com.libraryofalexandria.cards.data.transformer

import com.libraryofalexandria.cards.domain.Filters
import com.libraryofalexandria.cards.domain.Type
import org.junit.Assert.assertEquals
import org.junit.Test

class ExpansionFilterMapperTest {

    private val mapper = ExpansionFilterMapper()

    @Test
    fun `should be CORE`(){
        val filters = arrayListOf(Type.CORE, Type.EXPANSION, Type.MASTERS, Type.DRAFT_INNOVATION, Type.FUNNY)
        filters.forEach {
            assertEquals(Filters.Expansion.CORE, mapper.transform(it))
        }
    }

    @Test
    fun `should be PROMO`(){
        val filters = arrayListOf(Type.MASTERPIECE, Type.PROMO)
        filters.forEach {
            assertEquals(Filters.Expansion.PROMO, mapper.transform(it))
        }
    }

    @Test
    fun `should be SUPPLEMENTAL`(){
        val filters = arrayListOf(Type.BOX, Type.DUEL_DECK, Type.COMMANDER, Type.PLANECHASE, Type.ARCHENEMY, Type.FROM_THE_VAULT, Type.SPELLBOOK, Type.PREMIUM_DECK, Type.STARTER)
        filters.forEach {
            assertEquals(Filters.Expansion.SUPPLEMENTAL, mapper.transform(it))
        }
    }

    @Test
    fun `should be OTHER`(){
        val filters = arrayListOf(Type.TOKEN, Type.VANGUARD, Type.MEMORABILIA, Type.TREASURE_CHEST)
        filters.forEach {
            assertEquals(Filters.Expansion.OTHER, mapper.transform(it))
        }
    }
}