package com.libraryofalexandria.cards.data.local

import com.libraryofalexandria.cards.domain.Set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlin.coroutines.CoroutineContext

class SetsLocalDataSource {

    private var map = mutableListOf<Set>()

    fun list(): List<Set> = map.toList()

    fun store(sets: List<Set>) = with(sets) {
        filterNot { map.contains(it) }
            .forEach { map.add(it) }
        list()
    }
}