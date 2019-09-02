package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.data.local.FiltersLocalDataSource
import com.libraryofalexandria.core.base.Repository

class FiltersRepository(
    private val localDataSource: FiltersLocalDataSource
) : Repository() {

    suspend fun list() = localDataSource.list()
}