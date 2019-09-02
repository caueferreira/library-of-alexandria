package com.libraryofalexandria.cards.view.expansions.transformers

import com.libraryofalexandria.R
import com.libraryofalexandria.cards.domain.Filters
import com.libraryofalexandria.cards.view.expansions.ui.FilterViewEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class ExpansionFilterViewEntityMapperTest{
    private val other = FilterViewEntity(
        R.drawable.ic_local_library,
        android.R.color.holo_blue_dark,
        com.libraryofalexandria.cards.view.R.string.other,
        Filters.Expansion.OTHER
    )

    private val supplemental = FilterViewEntity(
        com.libraryofalexandria.R.drawable.ic_local_library,
        android.R.color.holo_orange_dark,
        com.libraryofalexandria.cards.view.R.string.supplemental,
        Filters.Expansion.SUPPLEMENTAL
    )

    private val draft = FilterViewEntity(
        com.libraryofalexandria.R.drawable.ic_local_library,
        android.R.color.holo_purple,
        com.libraryofalexandria.cards.view.R.string.promo,
        Filters.Expansion.PROMO
    )

    private val core = FilterViewEntity(
        com.libraryofalexandria.R.drawable.ic_local_library,
        android.R.color.holo_green_dark,
        com.libraryofalexandria.cards.view.R.string.core,
        Filters.Expansion.CORE
    )

    private val mapper = ExpansionFilterViewEntityMapper()

    @Test
    fun `should be core`(){
        val actual = mapper.transform(Filters.Expansion.CORE)
        assertEquals(core, actual)
    }

    @Test
    fun `should be draft`(){
        val actual = mapper.transform(Filters.Expansion.PROMO)
        assertEquals(draft, actual)
    }

    @Test
    fun `should be supplemental`(){
        val actual = mapper.transform(Filters.Expansion.SUPPLEMENTAL)
        assertEquals(supplemental, actual)
    }

    @Test
    fun `should be other`(){
        val actual = mapper.transform(Filters.Expansion.OTHER)
        assertEquals(other, actual)
    }
}