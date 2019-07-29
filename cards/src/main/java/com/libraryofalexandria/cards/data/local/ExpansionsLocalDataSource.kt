package com.libraryofalexandria.cards.data.local

import com.libraryofalexandria.cache.Cache
import com.libraryofalexandria.cards.domain.Expansion

class ExpansionsLocalDataSource(private val cache: Cache<Expansion>) {

    fun list(): List<Expansion> = cache.list()
    fun store(expansions: List<Expansion>) = cache.store(expansions)
}