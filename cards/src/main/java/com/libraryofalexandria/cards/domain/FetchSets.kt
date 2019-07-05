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
                remote.list().onSuccess {
                    emit(FetchResult.Update(local.store(it.toList())))
                }.onFailure {
                    throw it
                }
            }
        }

    sealed class FetchResult {
        abstract val result: Result<List<Set>>

        data class Cache(override val result: Result<List<Set>>) : FetchResult()
        data class Update(override val result: Result<List<Set>>) : FetchResult()
    }
}