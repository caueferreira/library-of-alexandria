package com.libraryofalexandria.cards.view.expansions.transformers

import com.libraryofalexandria.cards.data.local.entity.Filters
import com.libraryofalexandria.cards.data.transformer.ExpansionFilterMapper
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.cards.view.expansions.ui.ExpansionViewEntity

class ExpansionViewEntityMapper(
    private val mapper: ExpansionFilterViewEntityMapper = ExpansionFilterViewEntityMapper(),
    private val filterMapper: ExpansionFilterMapper = ExpansionFilterMapper()
) {

    fun transform(expansion: Expansion): ExpansionViewEntity {
        val filter = filterMapper.transform(expansion.type)
        return ExpansionViewEntity(
            expansion.id,
            expansion.code,
            expansion.name,
            "Total cards ${expansion.totalCards}",
            expansion.totalCards,
            expansion.iconUri,
            expansion.type.name,
            colorFromType(filter),
            fontColorFromType(filter),
            mapper.transform(filter)
        )
    }

    private fun colorFromType(filter: Filters.Expansion): Int =
        when (filter) {
            Filters.Expansion.CORE -> android.R.color.holo_green_dark
            Filters.Expansion.PROMO -> android.R.color.holo_purple
            Filters.Expansion.SUPPLEMENTAL -> android.R.color.holo_orange_dark
            else -> android.R.color.holo_blue_dark
        }

    private fun fontColorFromType(filter: Filters.Expansion): Int =
        when (filter) {
            Filters.Expansion.CORE -> android.R.color.black
            Filters.Expansion.SUPPLEMENTAL -> android.R.color.black
            else -> android.R.color.white
        }
}

