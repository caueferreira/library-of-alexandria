package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.data.local.ExpansionsLocalDataSource
import com.libraryofalexandria.cards.data.network.ExpansionsRemoteDataSource
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.cards.domain.ExpansionResult
import com.libraryofalexandria.core.base.Repository
import com.libraryofalexandria.core.base.RepositoryStrategy
import com.libraryofalexandria.network.exception.NetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExpansionRepository(
    private val remote: ExpansionsRemoteDataSource,
    private val local: ExpansionsLocalDataSource
) : Repository() {

    suspend fun list(strategy: RepositoryStrategy): ExpansionResult = when (strategy) {
        RepositoryStrategy.CACHE -> fromCache()
        RepositoryStrategy.NETWORK -> fromNetwork()
    }

    private fun fromCache() = ExpansionResult.Success.Cache(local.list())

    private suspend fun fromNetwork() =
        try {
            ExpansionResult.Success.Network(local.store(remote.list()))
        } catch (e: NetworkError) {
            ExpansionResult.Failure(e)
        }
}