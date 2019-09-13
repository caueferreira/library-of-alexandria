package com.libraryofalexandria.cards.view.expansions.transformers

import com.libraryofalexandria.cards.domain.Filters
import com.libraryofalexandria.cards.view.R
import com.libraryofalexandria.cards.view.expansions.ui.FilterViewEntity

class ExpansionFilterViewEntityMapper {

    fun transform(filter: Filters.Expansion): FilterViewEntity =
        when (filter) {
            Filters.Expansion.CORE -> core()
            Filters.Expansion.PROMO -> draft()
            Filters.Expansion.SUPPLEMENTAL -> supplemental()
            Filters.Expansion.OTHER -> other()
        }

    private fun other() = FilterViewEntity(
        com.libraryofalexandria.R.drawable.ic_local_library,
        android.R.color.holo_blue_dark,
        R.string.other,
        Filters.Expansion.OTHER
    )

    private fun supplemental() = FilterViewEntity(
        com.libraryofalexandria.R.drawable.ic_local_library,
        android.R.color.holo_orange_dark,
        R.string.supplemental,
        Filters.Expansion.SUPPLEMENTAL
    )

    private fun draft() = FilterViewEntity(
        com.libraryofalexandria.R.drawable.ic_local_library,
        android.R.color.holo_purple,
        R.string.promo,
        Filters.Expansion.PROMO
    )

    private fun core() = FilterViewEntity(
        com.libraryofalexandria.R.drawable.ic_local_library,
        android.R.color.holo_green_dark,
        R.string.core,
        Filters.Expansion.CORE
    )
}