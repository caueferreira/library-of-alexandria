package com.libraryofalexandria.cards.data.transformer

import com.libraryofalexandria.cards.data.local.entity.Filters
import com.libraryofalexandria.cards.domain.Type

class SetFilterMapper {
    fun transform(type: Type): Filters.Sets =
        when (type) {
            Type.CORE, Type.EXPANSION, Type.MASTERS, Type.DRAFT_INNOVATION, Type.FUNNY -> Filters.Sets.CORE
            Type.MASTERPIECE, Type.PROMO -> Filters.Sets.PROMO
            Type.BOX, Type.DUEL_DECK, Type.COMMANDER, Type.PLANECHASE, Type.ARCHENEMY, Type.FROM_THE_VAULT, Type.SPELLBOOK, Type.PREMIUM_DECK, Type.STARTER -> Filters.Sets.SUPPLEMENTAL
            else -> Filters.Sets.OTHER
        }
}