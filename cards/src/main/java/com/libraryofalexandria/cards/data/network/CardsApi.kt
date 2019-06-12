package com.libraryofalexandria.cards.data.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query


interface CardsApi {

    @GET("cards")
    fun get(@Query("page") page: Int): Deferred<RootResponse>
}