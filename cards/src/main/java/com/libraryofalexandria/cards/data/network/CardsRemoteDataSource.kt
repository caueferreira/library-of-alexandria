package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.transformer.CardMapper
import com.libraryofalexandria.cards.domain.Card
import com.libraryofalexandria.network.exception.NetworkError
import com.libraryofalexandria.network.handler.NetworkHandler
import com.libraryofalexandria.network.handler.handleErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CardsRemoteDataSource(
    private val api: ScryfallApi,
    private val mapper: CardMapper = CardMapper()
) {
    suspend fun list(expansion: String, page: Int): List<Card> =
        handleErrors(NetworkHandler()) {
            api.cards(
                "set=$expansion",
                page
            ).data
                .filter { it.language == "en" }
                .map { mapper.transform(it) }
        }
}