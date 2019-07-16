package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.cards.data.network.CardsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchCards(
    private val remote: CardsRemoteDataSource
) {

    suspend fun fetch(expansion: String, page: Int): Flow<CardResult> = flow {
        try {
            emit(CardResult.Success.Network(remote.list(expansion, page)))
        } catch (e: Exception) {
            emit(CardResult.Failure(e))
        }
    }
}
