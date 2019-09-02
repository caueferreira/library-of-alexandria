package com.libraryofalexandria.cards.data.transformer

import com.libraryofalexandria.cards.data.network.entity.ExpansionResponse
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.cards.domain.Type
import java.util.*

class ExpansionMapper(private val filterMapper: ExpansionFilterMapper = ExpansionFilterMapper()) {

    fun transform(response: ExpansionResponse): Expansion =
        Expansion(
            response.id,
            response.code,
            response.name,
            Date(),
            Type.valueOf(response.type.toUpperCase()),
            response.totalCards,
            response.iconUri,
            filterMapper.transform(Type.valueOf(response.type.toUpperCase()))
        )
}