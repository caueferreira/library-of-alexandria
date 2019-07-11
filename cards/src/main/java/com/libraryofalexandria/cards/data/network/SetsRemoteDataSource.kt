package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.transformer.SetMapper
import com.libraryofalexandria.cards.domain.Set
import com.libraryofalexandria.network.handler.NetworkHandler
import com.libraryofalexandria.network.handler.handleErrors

class SetsRemoteDataSource(
    private val api: ScryfallApi,
    private val mapper: SetMapper = SetMapper()
) {
    suspend fun list(): List<Set> =
        api.sets().data
            .map { mapper.transform(it) }
}