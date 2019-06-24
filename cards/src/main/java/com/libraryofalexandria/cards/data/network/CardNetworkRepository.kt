package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.transformer.CardMapper
import com.libraryofalexandria.cards.domain.Card
import com.libraryofalexandria.network.handler.NetworkHandler
import com.libraryofalexandria.network.handler.handleNetworkErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CardNetworkRepository(
    private val api: ScryfallApi,
    private val mapper: CardMapper = CardMapper(),
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
    private val handler: NetworkHandler = NetworkHandler()
) {
    suspend fun list(set: String, page: Int): Result<Flow<Card>> =
        withContext(coroutineContext) {
            runCatching {

                api.cards(
                    "set=$set",
                    page
                ).data.asFlow()
                    .filter { it.language == "en" }
                    .map { mapper.transform(it) }
            }.handleNetworkErrors(handler)
        }
}