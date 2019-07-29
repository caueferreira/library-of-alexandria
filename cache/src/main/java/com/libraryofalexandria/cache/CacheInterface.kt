package com.libraryofalexandria.cache

interface CacheInterface<K, V> {

    fun put(value: V)
    fun putAll(values: List<V>)
    fun clear()
    fun getAll(): List<V>?
    fun get(key: K): V?
}