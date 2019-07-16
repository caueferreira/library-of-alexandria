package com.libraryofalexandria.cards.domain

import java.util.*

data class Card(
    val id: String,
    val name: String,
    val number: String,
    val text: String?,
    val flavor: String?,
    val type: String,
    val prices: Prices,
    val rarity: Rarity,
    val manaCost: ManaCost,
    val releasedAt: Date,
    val images: Images,
    val expansion: CardExpansion
) {
    override fun toString(): String {
        return "Card(id='$id', name='$name', number='$number', plainText=$text, flavor=$flavor, type='$type', prices=$prices, rarity=$rarity, manaCost=$manaCost, releasedAt=$releasedAt, images=$images, expansion=$expansion)"
    }
}

data class ManaCost(
    val plain: String,
    val cmc: Double,
    val identity: List<Color>,
    val cost: List<Color>
) {
    override fun toString(): String {
        return "ManaCost(plain='$plain', cmc=$cmc, identity=$identity, plainCost=$cost)"
    }
}

data class CardExpansion(
    val id: String,
    val name: String,
    val uri: String
) {
    override fun toString(): String {
        return "CardExpansion(id='$id', name='$name', uri='$uri')"
    }
}

data class Prices(
    val usd: Double?,
    val eur: Double?,
    val usdFoil: Double?
) {
    override fun toString(): String {
        return "Prices(usd=$usd, eur=$eur, usdFoil=$usdFoil)"
    }
}

data class Images(
    val small: String,
    val normal: String,
    val large: String,
    val png: String,
    val artCrop: String,
    val borderCrop: String
) {
    override fun toString(): String {
        return "Images(small='$small', normal='$normal', large='$large', png='$png', artCrop='$artCrop', borderCrop='$borderCrop')"
    }
}

enum class Rarity {
    COMMON, UNCOMMON, RARE, MYTHIC, TIMESHIFTED, MASTERPIECE
}

enum class Color { COLORLESS, WHITE, BLUE, BLACK, RED, GREEN }
