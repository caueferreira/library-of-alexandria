package com.libraryofalexandria.cards.view.expansions

import android.view.View
import com.libraryofalexandria.cards.view.expansions.ui.ExpansionViewEntity
import com.libraryofalexandria.cards.view.expansions.ui.FilterViewEntity
import com.libraryofalexandria.core.base.State

sealed class ExpansionState : State() {

    sealed class Expansions : ExpansionState() {
        data class Loading(val visibility: Int = View.VISIBLE) : Expansions()
        data class Loaded(
            val isUpdate: Boolean = false,
            val loadingVisibility: Int = View.INVISIBLE,
            val errorVisibility: Int = View.INVISIBLE,
            val expansions: List<ExpansionViewEntity>
        ) :
            Expansions()

        sealed class Error {
            data class Generic(
                val loadingVisibility: Int = View.INVISIBLE,
                val errorVisibility: Int = View.VISIBLE,
                val message: String
            ) : Expansions()
        }
    }

    sealed class Filters : ExpansionState() {
        data class Loaded(val loadingVisibility: Int = View.INVISIBLE, val filters: List<FilterViewEntity>) :
            Filters()
    }
}