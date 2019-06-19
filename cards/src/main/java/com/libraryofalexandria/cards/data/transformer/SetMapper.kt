package com.libraryofalexandria.cards.data.transformer

import com.libraryofalexandria.cards.data.network.entity.SetResponse
import com.libraryofalexandria.cards.domain.Set
import com.libraryofalexandria.cards.domain.Type
import java.util.*

class SetMapper {

    fun transform(response: SetResponse): Set =
        Set(
            response.id,
            response.code,
            response.name,
            Date(),
            Type.valueOf(response.type.toUpperCase()),
            response.totalCards,
            response.iconUri
        )
}