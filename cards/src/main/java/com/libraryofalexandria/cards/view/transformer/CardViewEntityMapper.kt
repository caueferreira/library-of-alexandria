package com.libraryofalexandria.cards.view.transformer

import com.libraryofalexandria.cards.domain.Card
import com.libraryofalexandria.cards.view.activity.CardViewEntity

class CardViewEntityMapper {

    fun transform(card: Card): CardViewEntity =
        CardViewEntity(
            card.id,
            card.name,
            card.manaCost.plain,
            card.images.normal,
            card.type,
            card.rarity.name,
            card.text,
            card.flavor,
            card.images.artCrop
        )
}