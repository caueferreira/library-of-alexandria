package com.libraryofalexandria.cards.domain

import com.libraryofalexandria.core.Result

sealed class SetsResult : Result() {
    object Loading : SetsResult()

    sealed class Success {
        data class Cache(val result: List<Set>) : SetsResult()
        data class Network(val result: List<Set>) : SetsResult()
    }

    data class Failure(val message: Int) : SetsResult()
}