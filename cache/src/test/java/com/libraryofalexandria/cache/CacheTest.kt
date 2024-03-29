package com.libraryofalexandria.cache

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.doNothing

class CacheTest {

    private val context: Context = Mockito.mock(Context::class.java)
    private val sharedPrefs = Mockito.mock(SharedPreferences::class.java)
    private val editor = Mockito.mock(SharedPreferences.Editor::class.java)

    private val cache = Cache(context, "", TestObject::class.java, sharedPrefs)

    @Test
    fun `should return empty list`() {
        val result = CacheBuilder()
            .empty()
            .list()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return stored data`() {
        val data = arrayListOf(TestObject("1"), TestObject("2"), TestObject("3"))
        val result = CacheBuilder()
            .store(data)
            .list()

        assertEquals(data, result)
    }

    @Test
    fun `should not store twice same data`() {
        val data = arrayListOf(TestObject("1"), TestObject("2"), TestObject("3"))
        val result = CacheBuilder()
            .store(data)
            .store(data)
            .list()

        assertEquals(data, result)
    }

    private data class TestObject(val id: String)

    private inner class CacheBuilder {

        fun store(list: List<TestObject>): CacheBuilder {
            whenever(sharedPrefs.edit()).thenReturn(editor)
            whenever(editor.putString(any(), any())).thenReturn(editor)
            doNothing().`when`(editor).apply()
            cache.store(list)

            whenever(sharedPrefs.contains(any())).thenReturn(true)
            whenever(sharedPrefs.getString(any(), any())).thenReturn(Gson().toJson(list))
            return this
        }

        fun empty(): CacheBuilder {
            whenever(sharedPrefs.contains(any())).thenReturn(false)
            return this
        }

        fun list() = cache.list()
    }
}