package com.libraryofalexandria.cards.view.sets.transformers

import com.libraryofalexandria.cards.data.local.entity.Filters
import com.libraryofalexandria.cards.view.R
import com.libraryofalexandria.cards.view.sets.ui.FilterViewEntity

class SetsFilterViewEntityMapper {

    fun transform(filter: Filters.Sets): FilterViewEntity =
        when (filter) {
            Filters.Sets.CORE -> core()
            Filters.Sets.PROMO -> draft()
            Filters.Sets.SUPPLEMENTAL -> supplemental()
            Filters.Sets.OTHER -> other()
        }


    private fun other() = FilterViewEntity(
        com.libraryofalexandria.R.drawable.ic_local_library,
        android.R.color.holo_blue_dark,
        R.string.other,
        Filters.Sets.OTHER
    )


    private fun supplemental() = FilterViewEntity(
        com.libraryofalexandria.R.drawable.ic_local_library,
        android.R.color.holo_orange_dark,
        R.string.supplemental,
        Filters.Sets.SUPPLEMENTAL
    )


    private fun draft() = FilterViewEntity(
        com.libraryofalexandria.R.drawable.ic_local_library,
        android.R.color.holo_purple,
        R.string.promo,
        Filters.Sets.PROMO
    )


    private fun core() = FilterViewEntity(
        com.libraryofalexandria.R.drawable.ic_local_library,
        android.R.color.holo_green_dark,
        R.string.core,
        Filters.Sets.CORE
    )
}