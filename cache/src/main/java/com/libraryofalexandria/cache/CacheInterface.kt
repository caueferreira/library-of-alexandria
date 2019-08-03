package com.libraryofalexandria.cache

interface CacheInterface<T> {

    fun list(): List<T>
    fun store(list: List<T>): List<T>
    fun store(t: T): T
}