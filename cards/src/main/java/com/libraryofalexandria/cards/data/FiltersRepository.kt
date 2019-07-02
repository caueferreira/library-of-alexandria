package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.data.local.FiltersLocalDataSource
import com.libraryofalexandria.cards.view.sets.transformers.SetsFilterViewEntityMapper
import kotlinx.coroutines.flow.map

class FiltersRepository(
    private val localDataSource: FiltersLocalDataSource,
    private val mapper: SetsFilterViewEntityMapper = SetsFilterViewEntityMapper()
) {

    suspend fun get() = localDataSource.get().map { it.map { mapper.transform(it) } }
}