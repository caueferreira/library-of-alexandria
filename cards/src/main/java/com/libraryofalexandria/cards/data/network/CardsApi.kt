package com.libraryofalexandria.cards.data.network

import retrofit2.http.GET
import retrofit2.http.Query


interface CardsApi {

    @GET("cards")
    suspend fun get(@Query("page") page: Int): RootResponse
}