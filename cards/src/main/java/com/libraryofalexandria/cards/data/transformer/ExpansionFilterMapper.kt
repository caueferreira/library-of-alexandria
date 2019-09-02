package com.libraryofalexandria.cards.data.transformer

import com.libraryofalexandria.cards.domain.Filters
import com.libraryofalexandria.cards.domain.Type

class ExpansionFilterMapper {
    fun transform(type: Type): Filters.Expansion =
        when (type) {
            Type.CORE, Type.EXPANSION, Type.MASTERS, Type.DRAFT_INNOVATION, Type.FUNNY -> Filters.Expansion.CORE
            Type.MASTERPIECE, Type.PROMO -> Filters.Expansion.PROMO
            Type.BOX, Type.DUEL_DECK, Type.COMMANDER, Type.PLANECHASE, Type.ARCHENEMY, Type.FROM_THE_VAULT, Type.SPELLBOOK, Type.PREMIUM_DECK, Type.STARTER -> Filters.Expansion.SUPPLEMENTAL
            else -> Filters.Expansion.OTHER
        }
}