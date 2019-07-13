package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.cards.data.local.ExpansionsLocalDataSource
import com.libraryofalexandria.cards.data.network.ExpansionsRemoteDataSource
import kotlinx.coroutines.flow.*

class FetchExpansions(
    private val remote: ExpansionsRemoteDataSource,
    private val local: ExpansionsLocalDataSource
) {

    suspend fun fetch(): Flow<ExpansionResult> = flow {
        val result = local.list()

        val hasCache = result.isNotEmpty()
        if (hasCache) {
            emit(ExpansionResult.Success.Cache(result))
        }
        try {
            emit(ExpansionResult.Success.Network(local.store(remote.list()), hasCache))
        } catch (e: Exception) {
            emit(ExpansionResult.Failure(e))
        }
    }
}
