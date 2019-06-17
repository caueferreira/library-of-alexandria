package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.domain.Card
import kotlinx.coroutines.flow.Flow

interface CardsRepository {
    suspend fun get(page: Int): Result<Flow<Card>>
}