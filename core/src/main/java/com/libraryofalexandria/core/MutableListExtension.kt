package com.libraryofalexandria.core

fun <T> MutableList<T>.addOrRemove(t: T) {
    if (contains(t)) {
        remove(t)
    } else {
        add(t)
    }
}