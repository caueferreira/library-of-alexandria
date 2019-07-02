package com.libraryofalexandria.cards.view.sets.transformers

import com.libraryofalexandria.cards.data.local.entity.Filters
import com.libraryofalexandria.cards.data.transformer.SetFilterMapper
import com.libraryofalexandria.cards.domain.Set
import com.libraryofalexandria.cards.view.sets.ui.SetViewEntity

class SetViewEntityMapper(
    private val mapper: SetsFilterViewEntityMapper = SetsFilterViewEntityMapper(),
    private val filterMapper: SetFilterMapper = SetFilterMapper()
) {

    fun transform(set: Set): SetViewEntity {
        val filter = filterMapper.transform(set.type)
        return SetViewEntity(
            set.id,
            set.code,
            set.name,
            "Total cards ${set.totalCards}",
            set.totalCards,
            set.iconUri,
            set.type.name,
            colorFromType(filter),
            fontColorFromType(filter),
            mapper.transform(filter)
        )
    }

    private fun colorFromType(filter: Filters.Sets): Int =
        when (filter) {
            Filters.Sets.CORE -> android.R.color.holo_green_dark
            Filters.Sets.PROMO -> android.R.color.holo_purple
            Filters.Sets.SUPPLEMENTAL -> android.R.color.holo_orange_dark
            else -> android.R.color.holo_blue_dark
        }

    private fun fontColorFromType(filter: Filters.Sets): Int =
        when (filter) {
            Filters.Sets.CORE -> android.R.color.black
            Filters.Sets.SUPPLEMENTAL -> android.R.color.black
            else -> android.R.color.white
        }
}

