package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.data.local.FiltersLocalDataSource
import com.libraryofalexandria.cards.view.sets.transformers.SetsFilterViewEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class FiltersRepository(
    private val localDataSource: FiltersLocalDataSource,
    private val mapper: SetsFilterViewEntityMapper = SetsFilterViewEntityMapper()
) {

    suspend fun get() = flow {
        emit(localDataSource.get().map { mapper.transform(it) })
    }
}