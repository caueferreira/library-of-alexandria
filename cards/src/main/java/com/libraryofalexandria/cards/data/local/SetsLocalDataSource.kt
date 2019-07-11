package com.libraryofalexandria.cards.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.libraryofalexandria.cards.domain.Set
import com.squareup.moshi.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlin.coroutines.CoroutineContext

class SetsLocalDataSource(val context: Context) {

    private val PREF_NAME = "demo-local-datasource"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private var map = mutableListOf<Set>()

    fun list(): List<Set> {
        if (!sharedPref.contains(PREF_NAME))
            return listOf()

        return Gson().fromJson(sharedPref.getString(PREF_NAME, "[]"), object : TypeToken<ArrayList<Set>>() {}.type)
    }

    fun store(sets: List<Set>) = with(sets) {
        filterNot { map.contains(it) }
            .forEach { map.add(it) }

        val editor = sharedPref.edit()
        editor.putString(PREF_NAME, Gson().toJson(this))
        editor.apply()

        list()
    }
}