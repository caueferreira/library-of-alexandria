package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.cards.data.local.SetsLocalDataSource
import com.libraryofalexandria.cards.data.network.SetsRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FetchSets(
    private val remote: SetsRemoteDataSource,
    private val local: SetsLocalDataSource
) {

    suspend fun fetch(): Flow<SetsResult> = flow {
        emit(SetsResult.Loading)
        val result = local.list()

        if (result.isNotEmpty()) {
            emit(SetsResult.Success.Cache(result))
        }
        try {
            emit(SetsResult.Success.Network(local.store(remote.list())))
        } catch (e: Exception) {
            emit(SetsResult.Failure(e))
        }
    }
}