package com.libraryofalexandria.cards.view.sets.ui

import android.view.View
import com.libraryofalexandria.core.ui.ViewState

data class SetsViewState(
    val isLoading: Int = View.INVISIBLE,
    val isError: Int = View.GONE,
    val sets: List<SetViewEntity> = emptyList(),
    val throwable: Throwable? = null,
    val isUpdate: Int = View.GONE
) : ViewState()
