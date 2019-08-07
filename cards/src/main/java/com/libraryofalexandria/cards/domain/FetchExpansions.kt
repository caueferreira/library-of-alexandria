package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.cards.data.ExpansionRepository
import com.libraryofalexandria.core.base.RepositoryStrategy
import kotlinx.coroutines.flow.flow

class FetchExpansions(
    private val repository: ExpansionRepository
) {

    suspend fun fetch() = flow {
        emit(repository.list(RepositoryStrategy.CACHE))
        emit(repository.list(RepositoryStrategy.NETWORK))
    }
}