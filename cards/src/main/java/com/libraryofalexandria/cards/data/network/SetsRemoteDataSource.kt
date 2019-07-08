package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.transformer.SetMapper
import com.libraryofalexandria.cards.domain.Set
import com.libraryofalexandria.network.handler.NetworkHandler
import com.libraryofalexandria.network.handler.handleErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SetsRemoteDataSource(
    private val api: ScryfallApi,
    private val mapper: SetMapper = SetMapper(),
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
    private val handler: NetworkHandler = NetworkHandler()
) {
    suspend fun list(): List<Set> =
        handleErrors(handler) {
            withContext(coroutineContext) {
                api.sets().data
                    .map { mapper.transform(it) }
            }
        }
}