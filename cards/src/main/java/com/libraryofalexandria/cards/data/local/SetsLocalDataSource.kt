package com.libraryofalexandria.cards.data.local

import com.libraryofalexandria.cards.domain.Set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlin.coroutines.CoroutineContext

class SetsLocalDataSource(private val coroutineContext: CoroutineContext = Dispatchers.IO) {

    private var map = mutableListOf<Set>()

    fun list(): Result<Flow<Set>> = with(coroutineContext) { runCatching { map.toMutableList().asFlow() } }

    fun store(sets: List<Set>) = with(coroutineContext) {
        sets
            .stream()
            .filter { !map.contains(it) }
            .forEach { map.add(it) }
        list()
    }
}