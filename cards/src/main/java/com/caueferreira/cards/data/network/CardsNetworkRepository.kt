package com.caueferreira.cards.data.network

import com.caueferreira.cards.data.CardsRepository
import com.caueferreira.cards.data.transformer.CardMapper
import com.caueferreira.cards.domain.Card
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class CardsNetworkRepository(
    private val api: CardsApi,
    private val mapper: CardMapper = CardMapper()
) : CardsRepository {
    override suspend fun get(page: Int): Result<List<Card>> =
        withContext(IO) {
            runCatching { api.get(page).await().data.filter { it.language == "en" }.map { mapper.transform(it) } }
        }
}