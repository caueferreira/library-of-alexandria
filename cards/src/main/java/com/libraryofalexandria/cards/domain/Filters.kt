package com.libraryofalexandria.cards.domain

sealed class Filters{

    enum class Expansion{
        CORE, PROMO, SUPPLEMENTAL, OTHER
    }
}