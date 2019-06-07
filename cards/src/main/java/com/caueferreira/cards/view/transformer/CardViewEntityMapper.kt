package com.caueferreira.cards.view.transformer

import com.caueferreira.cards.domain.Card
import com.caueferreira.cards.view.activity.CardViewEntity

class CardViewEntityMapper {

    fun transform(card: Card): CardViewEntity =
        CardViewEntity(card.id,
            card.name,
            card.manaCost.plain,
            card.images.normal,
            card.type,
            card.rarity.name,
            card.text,
            card.flavor,
            card.images.artCrop)
}