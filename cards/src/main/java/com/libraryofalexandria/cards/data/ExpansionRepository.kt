package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.data.local.ExpansionsLocalDataSource
import com.libraryofalexandria.cards.data.network.ExpansionsRemoteDataSource
import com.libraryofalexandria.cards.domain.ExpansionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExpansionRepository(
    private val remote: ExpansionsRemoteDataSource,
    private val local: ExpansionsLocalDataSource
) {

    suspend fun list(): Flow<ExpansionResult> = flow {
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