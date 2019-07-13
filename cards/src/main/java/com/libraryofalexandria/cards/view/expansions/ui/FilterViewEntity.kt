package com.libraryofalexandria.cards.view.expansions.ui

import com.libraryofalexandria.cards.data.local.entity.Filters

data class FilterViewEntity(
    val icon: Int,
    val iconColor: Int,
    val text: Int,
    val type: Filters.Expansion
)