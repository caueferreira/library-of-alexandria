package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.cards.data.FiltersRepository
import kotlinx.coroutines.flow.flowOf

class FetchFilters(private val repository: FiltersRepository) {

    suspend fun fetch() = flowOf(repository.list())
}