package com.libraryofalexandria.cards.view.expansions.ui

import android.view.View
import com.libraryofalexandria.core.base.ViewState

sealed class ExpansionViewState : ViewState() {

    sealed class Expansions {
        data class Loading(val visibility: Int = View.VISIBLE) : ExpansionViewState()
        data class Loaded(
            val isUpdate: Boolean = false,
            val loadingVisibility: Int = View.INVISIBLE,
            val errorVisibility: Int = View.INVISIBLE,
            val expansions: List<ExpansionViewEntity>
        ) :
            ExpansionViewState()

        sealed class Error {
            data class Generic(
                val loadingVisibility: Int = View.INVISIBLE,
                val errorVisibility: Int = View.VISIBLE,
                val message: String
            ) : ExpansionViewState()
        }
    }

    sealed class Filters {
        data class Loading(val visibility: Int = View.VISIBLE) : ExpansionViewState()
        data class Loaded(val loadingVisibility: Int = View.INVISIBLE, val filters: List<FilterViewEntity>) :
            ExpansionViewState()

        sealed class Error {
            data class Generic(
                val loadingVisibility: Int = View.INVISIBLE,
                val errorVisibility: Int = View.VISIBLE,
                val message: String
            ) : ExpansionViewState()
        }
    }
}