package com.libraryofalexandria.cards.view.sets

import com.libraryofalexandria.core.Action

sealed class SetsAction : Action() {

    object FirstLoad : SetsAction()
    object LoadSets : SetsAction()
}