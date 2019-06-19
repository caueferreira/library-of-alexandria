package com.libraryofalexandria.cards.view.sets.transformers

import com.libraryofalexandria.cards.domain.Set
import com.libraryofalexandria.cards.domain.Type
import com.libraryofalexandria.cards.view.sets.ui.SetViewEntity

class SetViewEntityMapper {

    fun transform(set: Set): SetViewEntity =
        SetViewEntity(
            set.id,
            set.code,
            set.name,
            "Total cards ${set.totalCards}",
            set.iconUri,
            set.type.name,
            colorFromType(set.type),
            fontColorFromType(set.type)
        )

    private fun colorFromType(type: Type): Int =
        when (type) {
            Type.CORE, Type.EXPANSION, Type.MASTERS -> android.R.color.holo_green_dark
            Type.MASTERPIECE, Type.FROM_THE_VAULT, Type.SPELLBOOK, Type.PREMIUM_DECK, Type.TREASURE_CHEST, Type.PROMO -> android.R.color.holo_purple
            Type.DUEL_DECK, Type.COMMANDER, Type.PLANECHASE, Type.ARCHENEMY -> android.R.color.holo_orange_dark
            else -> android.R.color.holo_blue_dark
        }

    private fun fontColorFromType(type: Type): Int =
        when (type) {
            Type.DUEL_DECK, Type.COMMANDER, Type.PLANECHASE, Type.ARCHENEMY -> android.R.color.black
            Type.CORE, Type.EXPANSION, Type.MASTERS -> android.R.color.black
            else -> android.R.color.white
        }
}