package com.libraryofalexandria.core.extensions

fun <T> MutableList<T>.addOrRemove(t: T) {
    if (contains(t)) {
        remove(t)
    } else {
        add(t)
    }
}