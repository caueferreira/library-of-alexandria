package com.libraryofalexandria.cards.view.sets.ui

import android.view.View
import com.libraryofalexandria.core.ui.ViewState

data class SetsViewState(
    val isLoading: Int = View.INVISIBLE,
    val sets: List<SetViewEntity> = emptyList()
) : ViewState()