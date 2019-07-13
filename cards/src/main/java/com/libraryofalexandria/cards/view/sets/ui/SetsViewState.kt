package com.libraryofalexandria.cards.view.sets.ui

import android.view.View
import com.libraryofalexandria.core.ui.ViewState

sealed class SetsViewState : ViewState() {

    sealed class Sets {
        data class Loading(val visibility: Int = View.VISIBLE) : SetsViewState()
        data class SetsLoaded(
            val loadingVisibility: Int = View.INVISIBLE,
            val errorVisibility: Int = View.INVISIBLE,
            val sets: List<SetViewEntity>
        ) :
            SetsViewState()

        sealed class Error {
            data class Generic(
                val loadingVisibility: Int = View.INVISIBLE,
                val errorVisibility: Int = View.VISIBLE,
                val message: String
            ) : SetsViewState()
        }
    }

    sealed class Filters {
        data class Loading(val visibility: Int = View.VISIBLE) : SetsViewState()
        data class FiltersLoaded(val loadingVisibility: Int = View.INVISIBLE, val filters: List<FilterViewEntity>) :
            SetsViewState()

        sealed class Error {
            data class Generic(
                val loadingVisibility: Int = View.INVISIBLE,
                val errorVisibility: Int = View.VISIBLE,
                val message: String
            ) : SetsViewState()
        }
    }
}