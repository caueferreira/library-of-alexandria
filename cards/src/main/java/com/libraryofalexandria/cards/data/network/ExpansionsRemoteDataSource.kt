package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.transformer.ExpansionMapper
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.network.exception.NetworkError
import com.libraryofalexandria.network.handler.NetworkHandler
import com.libraryofalexandria.network.handler.handleErrors

class ExpansionsRemoteDataSource(
    private val api: ScryfallApi,
    private val mapper: ExpansionMapper = ExpansionMapper()
) {

    @Throws(NetworkError::class)
    suspend fun list(): List<Expansion> =
        handleErrors(NetworkHandler()) {
            api.expansions().data
                .map { mapper.transform(it) }
        }
}