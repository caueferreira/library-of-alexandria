package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.data.local.ExpansionsLocalDataSource
import com.libraryofalexandria.cards.data.network.ExpansionsRemoteDataSource
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.core.base.Repository
import com.libraryofalexandria.core.base.RepositoryStrategy
import com.libraryofalexandria.network.exception.NetworkError

class ExpansionRepository(
    private val remote: ExpansionsRemoteDataSource,
    private val local: ExpansionsLocalDataSource
) : Repository() {

    @Throws(NetworkError::class)
    suspend fun list(strategy: RepositoryStrategy): List<Expansion> = when (strategy) {
        RepositoryStrategy.CACHE -> fromCache()
        RepositoryStrategy.NETWORK -> fromNetwork()
    }

    fun store(list: List<Expansion>) = local.store(list)

    private fun fromCache() = local.list()

    private suspend fun fromNetwork() = remote.list()
}