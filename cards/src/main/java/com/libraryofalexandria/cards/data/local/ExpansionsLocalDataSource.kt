package com.libraryofalexandria.cards.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.libraryofalexandria.cards.domain.Expansion

class ExpansionsLocalDataSource(val context: Context) {

    private val prefName = "ExtensionsDataSource"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    private var map = mutableListOf<Expansion>()

    fun list(): List<Expansion> {
        if (!sharedPref.contains(prefName))
            return listOf()

        return Gson().fromJson(sharedPref.getString(prefName, "[]"), object : TypeToken<ArrayList<Expansion>>() {}.type)
    }

    fun store(expansions: List<Expansion>) = with(expansions) {
        filterNot { map.contains(it) }
            .forEach { map.add(it) }

        val editor = sharedPref.edit()
        editor.putString(prefName, Gson().toJson(this))
        editor.apply()

        list()
    }
}