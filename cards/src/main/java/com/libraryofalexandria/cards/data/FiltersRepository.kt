package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.data.local.FiltersLocalDataSource
import com.libraryofalexandria.cards.view.expansions.transformers.ExpansionFilterViewEntityMapper
import com.libraryofalexandria.core.base.Repository

class FiltersRepository(
    private val localDataSource: FiltersLocalDataSource,
    private val mapper: ExpansionFilterViewEntityMapper = ExpansionFilterViewEntityMapper()
) : Repository() {

    suspend fun list() = localDataSource.list().map { mapper.transform(it) }
}