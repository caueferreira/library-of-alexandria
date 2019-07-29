package com.libraryofalexandria.cache

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Cache<T>(
    private val context: Context,
    private val name: String,
    private val type: TypeToken<ArrayList<T>>
) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    private var map = mutableListOf<T>()

    fun list(): List<T> {
        if (!sharedPref.contains(name))
            return listOf()

        return Gson().fromJson(sharedPref.getString(name, "[]"), type.type)
    }

    fun store(expansions: List<T>) = with(expansions) {
        filterNot { map.contains(it) }
            .forEach { map.add(it) }

        val editor = sharedPref.edit()
        editor.putString(name, Gson().toJson(map))
        editor.apply()

        list()
    }
}