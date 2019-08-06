package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.cards.data.ExpansionRepository

class FetchExpansions(
    private val repository: ExpansionRepository
) {

    suspend fun fetch() = repository.list()
}
