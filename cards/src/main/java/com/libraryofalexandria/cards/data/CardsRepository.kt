package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.domain.Card

interface CardsRepository {
    suspend fun get(page: Int): Result<List<Card>>
}