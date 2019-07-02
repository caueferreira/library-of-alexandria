package com.libraryofalexandria.cards.data.local

import com.libraryofalexandria.cards.data.local.entity.Filters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FiltersLocalDataSource(private val coroutineContext: CoroutineContext = Dispatchers.IO) {

    private val filters = arrayListOf(
        Filters.Sets.CORE,
        Filters.Sets.PROMO,
        Filters.Sets.SUPPLEMENTAL,
        Filters.Sets.OTHER
    )

    suspend fun get() = withContext(coroutineContext) {
        runCatching {
            filters.asFlow()
        }
    }
}