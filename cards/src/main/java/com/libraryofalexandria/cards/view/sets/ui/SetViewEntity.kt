package com.libraryofalexandria.cards.view.sets.ui

data class SetViewEntity(
    val id: String,
    val code: String,
    val name: String,
    val totalCards: String,
    val iconUri: String,
    val type: String,
    val backgroundColor: Int,
    val textColor: Int,
    val setFilter: SetFilter
)

enum class SetFilter {
    CORE, DRAFT, CONSTRUCTED, OTHER
}