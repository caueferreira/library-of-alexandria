package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.transformer.CardMapper
import com.libraryofalexandria.cards.domain.Card
import com.libraryofalexandria.network.handler.NetworkHandler
import com.libraryofalexandria.network.handler.handleErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CardNetworkRepository(
    private val api: ScryfallApi,
    private val mapper: CardMapper = CardMapper(),
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
    private val handler: NetworkHandler = NetworkHandler()
) {
    suspend fun list(set: String, page: Int): Flow<List<Card>> =
        handleErrors(handler) {
            withContext(coroutineContext) {
                flow {
                    emit(api.cards(
                        "set=$set",
                        page
                    ).data
                        .filter { it.language == "en" }
                        .map { mapper.transform(it) })
                }
            }
        }
}