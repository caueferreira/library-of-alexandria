package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.CardsRepository
import com.libraryofalexandria.cards.data.transformer.CardMapper
import com.libraryofalexandria.cards.domain.Card
import com.libraryofalexandria.network.handler.handleNetworkErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CardsNetworkRepository(
    private val api: CardsApi,
    private val mapper: CardMapper = CardMapper(),
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) : CardsRepository {
    override suspend fun get(page: Int): Result<List<Card>> =
        withContext(coroutineContext) {
            runCatching {
                api.get(page).await().data
                    .filter { it.language == "en" }
                    .map { mapper.transform(it) }
            }.handleNetworkErrors()
        }
}