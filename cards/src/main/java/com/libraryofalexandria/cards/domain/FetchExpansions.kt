package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.cards.data.ExpansionRepository
import com.libraryofalexandria.core.base.RepositoryStrategy
import kotlinx.coroutines.flow.flow

class FetchExpansions(
    private val repository: ExpansionRepository
) {

    suspend fun fetch(strategies: List<RepositoryStrategy>) = flow {
        strategies.forEach {
            emit(repository.list(it))
        }
    }
}