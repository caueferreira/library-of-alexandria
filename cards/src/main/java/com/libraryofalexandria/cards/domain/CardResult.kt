package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.core.base.Result

sealed class CardResult : Result() {
    sealed class Success {
        data class Network(val result: List<Card>) : CardResult()
    }

    data class Failure(val error: Throwable) : CardResult()
}
