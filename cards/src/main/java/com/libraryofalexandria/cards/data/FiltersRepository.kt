package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.data.local.FiltersLocalDataSource
import com.libraryofalexandria.cards.view.expansions.transformers.ExpansionFilterViewEntityMapper
import kotlinx.coroutines.flow.flow

class FiltersRepository(
    private val localDataSource: FiltersLocalDataSource,
    private val mapper: ExpansionFilterViewEntityMapper = ExpansionFilterViewEntityMapper()
) {

    suspend fun get() = flow {
        emit(localDataSource.get().map { mapper.transform(it) })
    }
}