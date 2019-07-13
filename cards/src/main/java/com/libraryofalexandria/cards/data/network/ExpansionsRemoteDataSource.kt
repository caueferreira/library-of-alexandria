package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.transformer.ExpansionMapper
import com.libraryofalexandria.cards.domain.Expansion

class ExpansionsRemoteDataSource(
    private val api: ScryfallApi,
    private val mapper: ExpansionMapper = ExpansionMapper()
) {
    suspend fun list(): List<Expansion> =
        api.expansions().data
            .map { mapper.transform(it) }
}