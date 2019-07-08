package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.cards.data.local.SetsLocalDataSource
import com.libraryofalexandria.cards.data.network.SetsRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FetchSets(
    private val remote: SetsRemoteDataSource,
    private val local: SetsLocalDataSource,
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) {

    suspend fun fetch(): Flow<FetchResult> =
        withContext(coroutineContext) {
            flow {
                emit(FetchResult.Cache(local.list()))
                emit(FetchResult.Update(local.store(remote.list())))
            }
        }

    sealed class FetchResult {
        abstract val result: List<Set>

        data class Cache(override val result: List<Set>) : FetchResult()
        data class Update(override val result: List<Set>) : FetchResult()
    }
}