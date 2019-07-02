package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.data.local.FiltersLocalDataSource

class FiltersRepository(private val localDataSource: FiltersLocalDataSource) {

    suspend fun get() = localDataSource.get()
}