package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.transformer.SetMapper
import com.libraryofalexandria.cards.domain.Set
import com.libraryofalexandria.network.handler.NetworkHandler
import com.libraryofalexandria.network.handler.handleErrors

class SetsRemoteDataSource(
    private val api: ScryfallApi,
    private val mapper: SetMapper = SetMapper(),
    private val handler: NetworkHandler = NetworkHandler()
) {
    suspend fun list(): List<Set> =
        handleErrors(handler) {
            api.sets().data
                .map { mapper.transform(it) }
        }
}