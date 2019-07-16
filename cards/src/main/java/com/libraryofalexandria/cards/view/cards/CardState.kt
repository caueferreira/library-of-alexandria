package com.libraryofalexandria.cards.view.cards

import android.view.View
import com.libraryofalexandria.cards.view.cards.ui.CardViewEntity
import com.libraryofalexandria.cards.view.expansions.ui.ExpansionViewEntity
import com.libraryofalexandria.cards.view.expansions.ui.FilterViewEntity
import com.libraryofalexandria.core.base.Activities
import com.libraryofalexandria.core.base.State

sealed class CardState : State() {
    data class Loading(val visibility: Int = View.VISIBLE) : CardState()
    data class Loaded(
        val loadingVisibility: Int = View.INVISIBLE,
        val errorVisibility: Int = View.INVISIBLE,
        val cards: List<CardViewEntity>
    ) :
        CardState()

    sealed class Error {
        data class Generic(
            val loadingVisibility: Int = View.INVISIBLE,
            val errorVisibility: Int = View.VISIBLE,
            val message: String
        ) : CardState()
    }
}