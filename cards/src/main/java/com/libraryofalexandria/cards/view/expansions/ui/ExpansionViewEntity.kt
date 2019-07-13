package com.libraryofalexandria.cards.view.expansions.ui

data class ExpansionViewEntity(
    val id: String,
    val code: String,
    val name: String,
    val totalCards: String,
    val totalCardsPlain: Int,
    val iconUri: String,
    val type: String,
    val backgroundColor: Int,
    val textColor: Int,
    val filterViewEntity: FilterViewEntity
)