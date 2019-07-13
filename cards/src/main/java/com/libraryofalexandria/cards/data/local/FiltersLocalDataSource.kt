package com.libraryofalexandria.cards.data.local

import com.libraryofalexandria.cards.data.local.entity.Filters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FiltersLocalDataSource(private val coroutineContext: CoroutineContext = Dispatchers.IO) {

    private val filters = listOf(
        Filters.Expansion.CORE,
        Filters.Expansion.PROMO,
        Filters.Expansion.SUPPLEMENTAL,
        Filters.Expansion.OTHER
    )

    suspend fun get() = withContext(coroutineContext) {
        filters
    }
}