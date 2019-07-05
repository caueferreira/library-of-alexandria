package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.transformer.SetMapper
import com.libraryofalexandria.cards.domain.Set
import com.libraryofalexandria.network.handler.NetworkHandler
import com.libraryofalexandria.network.handler.handleNetworkErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SetsRemoteDataSource(
    private val api: ScryfallApi,
    private val mapper: SetMapper = SetMapper(),
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
    private val handler: NetworkHandler = NetworkHandler()
) {
    suspend fun list(): Result<List<Set>> =
        withContext(coroutineContext) {
            runCatching {
                api.sets().data
                    .map { mapper.transform(it) }
            }.handleNetworkErrors(handler)
        }
}