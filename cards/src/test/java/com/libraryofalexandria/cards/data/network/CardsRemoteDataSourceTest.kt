package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.network.entity.CardResponse
import com.libraryofalexandria.cards.data.network.entity.RootResponse
import com.libraryofalexandria.cards.data.transformer.CardMapper
import com.libraryofalexandria.cards.domain.Card
import com.libraryofalexandria.network.exception.NetworkError
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response

class CardsRemoteDataSourceTest {

    @Mock
    private lateinit var api: ScryfallApi
    @Mock
    private lateinit var mapper: CardMapper

    private lateinit var repositoryCard: CardsRemoteDataSource

    val emptyResponse = RootResponse<CardResponse>(arrayListOf())

    @Before
    fun `before each`() {
        MockitoAnnotations.initMocks(this)

        repositoryCard = CardsRemoteDataSource(api, mapper)
    }

    @Test
    fun `should return empty`() {
        runBlocking {
            val response = CardCardsRemoteDataSourceBuilder()
                .prepare(emptyResponse)
                .list("INV", 0)

            assertEquals(0, response.count())
            verify(api, times(1)).cards(any(), any())
            verifyZeroInteractions(mapper)
        }
    }

    @Test
    fun `should return empty because none was english`() {
        runBlocking {
            val card = mock<CardResponse> { CardResponse::class }

            val response = CardCardsRemoteDataSourceBuilder()
                .prepare(RootResponse(arrayListOf(card)))
                .list("INV", 0)

            whenever(card.language).thenReturn("pt")

            assertEquals(0, response.count())
            verify(api, times(1)).cards(any(), any())
            verifyZeroInteractions(mapper)
        }
    }

    @Test
    fun `should return list of mapped cards`() {
        runBlocking {
            val card = mock<CardResponse> { CardResponse::class }
            whenever(card.language).thenReturn("en")

            val response = CardCardsRemoteDataSourceBuilder()
                .prepare(RootResponse(arrayListOf(card, card, card, card, card)))
                .list("INV", 0)


            val total = response.onEach { card ->
                assertEquals(Card::class, card::class)
            }.count()

            assertEquals(5, total)

            verify(api, times(1)).cards(any(), any())
            verify(mapper, times(5)).transform(any())
        }
    }

    @Test(expected = NetworkError.Http.NotFound::class)
    fun `should propagate http network error`() {
        runBlocking {
            CardCardsRemoteDataSourceBuilder()
                .exception()
                .list("INV", 0)
        }
    }

    private inner class CardCardsRemoteDataSourceBuilder {

        suspend fun prepare(response: RootResponse<CardResponse>): CardCardsRemoteDataSourceBuilder {
            whenever(api.cards(any(), any())).thenReturn(response)
            if (response.data.isNotEmpty()) {
                whenever(mapper.transform(any())).thenReturn(mock { Card::class })
            }

            return this
        }

        suspend fun exception(): CardCardsRemoteDataSourceBuilder {
            whenever(api.cards(any(), any())).thenThrow(httpException("Not Found", 404))
            return this
        }

        suspend fun list(expansion: String, page: Int) = repositoryCard.list(expansion, page)

        private fun httpException(message: String, statusCode: Int): HttpException {
            val apiMessage = """{"code":$statusCode ,"message": "$message"}"""
            val jsonMediaType = MediaType.parse("application/json")
            val body = ResponseBody.create(jsonMediaType, apiMessage)
            return HttpException(Response.error<Any>(statusCode, body))
        }
    }
}