package com.libraryofalexandria.cache

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Cache<T>(
    private val context: Context,
    private val name: String,
    private val type: Class<T>,
    private val sharedPref: SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
) : CacheInterface<T> {

    private var map = mutableListOf<T>()
    private val empty = "[]"

    override fun list(): List<T> {
        if (!sharedPref.contains(name))
            return listOf()

        return Gson().fromJson(
            sharedPref.getString(name, empty),
            TypeToken.getParameterized(ArrayList::class.java, type).type
        )
    }

    override fun store(expansions: List<T>) = with(expansions) {
        filterNot { map.contains(it) }
            .forEach { map.add(it) }

        val editor = sharedPref.edit()
        editor.putString(name, Gson().toJson(map))
        editor.apply()

        list()
    }
}