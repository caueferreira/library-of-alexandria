package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.network.entity.CardResponse
import com.libraryofalexandria.cards.data.network.entity.ExpansionResponse
import com.libraryofalexandria.cards.data.network.entity.RootResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ScryfallApi {

    @GET("cards/search")
    suspend fun cards(
        @Query(
            value = "q",
            encoded = true
        ) q: String,
        @Query("page") page: Int
    ): RootResponse<CardResponse>

    @GET("sets")
    suspend fun expansions(): RootResponse<ExpansionResponse>
}