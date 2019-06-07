package com.caueferreira.cards.view.activity

data class CardViewEntity(
    val id: String,
    val name: String,
    val plainCost: String,
    val cardUrl: String,
    val type: String,
    val rarity: String,
    val plainText: String?,
    val flavor: String?,
    val artUrl: String
)