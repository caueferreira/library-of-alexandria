package com.libraryofalexandria.cards.data.local

import com.libraryofalexandria.R
import com.libraryofalexandria.cards.view.sets.ui.FilterViewEntity
import com.libraryofalexandria.cards.view.sets.ui.SetFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FiltersLocalDataSource(private val coroutineContext: CoroutineContext = Dispatchers.IO) {

    private val filters = arrayListOf(
        FilterViewEntity(
            R.drawable.ic_local_library,
            android.R.color.holo_green_dark,
            com.libraryofalexandria.cards.view.R.string.core,
            SetFilter.CORE
        ),
        FilterViewEntity(
            R.drawable.ic_local_library,
            android.R.color.holo_purple,
            com.libraryofalexandria.cards.view.R.string.draft,
            SetFilter.DRAFT
        ),
        FilterViewEntity(
            R.drawable.ic_local_library,
            android.R.color.holo_orange_dark,
            com.libraryofalexandria.cards.view.R.string.supplemental,
            SetFilter.SUPPLEMENTAL
        ),
        FilterViewEntity(
            R.drawable.ic_local_library,
            android.R.color.holo_blue_dark,
            com.libraryofalexandria.cards.view.R.string.other,
            SetFilter.OTHER
        )
    )

    suspend fun get() = withContext(coroutineContext) {
        runCatching {
            filters.asFlow()
        }
    }
}