package com.libraryofalexandria.cards.view.cards

import com.libraryofalexandria.core.base.Action

sealed class CardAction : Action() {
    data class FirstLoad(val expansion: String) : CardAction()
    object LoadMore : CardAction()
}