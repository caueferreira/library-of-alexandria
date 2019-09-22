package com.libraryofalexandria.cards.domain

data class Expansion(
    val id: String,
    val code: String,
    val name: String,
    val type: Type,
    val totalCards: Int,
    val iconUri: String,
    val filter: Filters.Expansion
)

enum class Type {
    CORE,
    EXPANSION,
    MASTERS,

    MASTERPIECE,
    FROM_THE_VAULT,
    SPELLBOOK,
    PREMIUM_DECK,
    TREASURE_CHEST,
    PROMO,

    DUEL_DECK,
    COMMANDER,
    PLANECHASE,
    ARCHENEMY,

    DRAFT_INNOVATION,
    VANGUARD,
    FUNNY,
    STARTER,
    BOX,
    TOKEN,
    MEMORABILIA
}