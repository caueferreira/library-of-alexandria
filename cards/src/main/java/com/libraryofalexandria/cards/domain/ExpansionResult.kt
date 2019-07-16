package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.core.base.Result

sealed class ExpansionResult : Result() {
    sealed class Success {
        data class Cache(val result: List<Expansion>) : ExpansionResult()
        data class Network(val  result: List<Expansion>, val isUpdate: Boolean) : ExpansionResult()
    }

    data class Failure(val error: Throwable) : ExpansionResult()
}
