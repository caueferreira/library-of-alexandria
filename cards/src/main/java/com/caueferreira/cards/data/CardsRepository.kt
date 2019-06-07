package com.caueferreira.cards.data

import com.caueferreira.cards.domain.Card

interface CardsRepository {
    suspend fun get(page: Int): Result<List<Card>>
}