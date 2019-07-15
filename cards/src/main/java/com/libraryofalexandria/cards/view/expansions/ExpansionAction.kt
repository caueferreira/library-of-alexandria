package com.libraryofalexandria.cards.view.expansions

import com.libraryofalexandria.core.base.Action

sealed class ExpansionAction : Action() {

    object FirstLoad : ExpansionAction()
    object LoadExpansion : ExpansionAction()
    object LoadFilters : ExpansionAction()
}