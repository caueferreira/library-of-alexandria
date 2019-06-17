package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.transformer.CardMapper
import com.libraryofalexandria.cards.domain.Card
import com.libraryofalexandria.network.exception.NetworkError
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.onEach
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

class CardNetworkRepositoryTest {

    @Mock
    private lateinit var api: CardsApi
    @Mock
    private lateinit var mapper: CardMapper

    private lateinit var repository: CardsNetworkRepository

    @Before
    fun `before each`() {
        MockitoAnnotations.initMocks(this)

        repository = CardsNetworkRepository(api, mapper)
    }

    @Test
    fun `should return empty`() {
        runBlocking {
            val response = RootResponse(arrayListOf())

            whenever(api.get(0)).thenReturn(response)

            repository.get(0).onSuccess { cards ->
                assertEquals(0, cards.count())
            }

            verify(api, times(1)).get(any())
            verifyZeroInteractions(mapper)
        }
    }

    @Test
    fun `should return empty because none was english`() {
        runBlocking {
            val card = mock<CardResponse> { CardResponse::class }
            val response = RootResponse(arrayListOf(card))

            whenever(card.language).thenReturn("pt")
            whenever(api.get(0)).thenReturn(response)

            repository.get(0).onSuccess { cards ->
                assertEquals(0, cards.count())
            }

            verify(api, times(1)).get(any())
            verifyZeroInteractions(mapper)
        }
    }

    @Test
    fun `should return list of mapped cards`() {
        runBlocking {
            val card = mock<CardResponse> { CardResponse::class }
            val response = RootResponse(arrayListOf(card, card, card, card, card))

            whenever(card.language).thenReturn("en")
            whenever(mapper.transform(any())).thenReturn(mock { Card::class })

            whenever(api.get(0)).thenReturn(response)

            repository.get(0).onSuccess { cards ->

                val total = cards.onEach { card ->
                    assertEquals(Card::class, card::class)
                }.count()

                assertEquals(5, total)
            }

            verify(api, times(1)).get(any())
            verify(mapper, times(5)).transform(any())
        }
    }

    @Test
    fun `should propagate http network error`() {
        runBlocking {
            whenever(api.get(0)).thenThrow(httpException("Not Found", 404))

            repository.get(0).onFailure {
                assertEquals(NetworkError.Http.NotFound, it)
            }
        }
    }

    private fun httpException(message: String, statusCode: Int): HttpException {
        val apiMessage = """{"code":$statusCode ,"message": "$message"}"""
        val jsonMediaType = MediaType.parse("application/json")
        val body = ResponseBody.create(jsonMediaType, apiMessage)
        return HttpException(Response.error<Any>(statusCode, body))
    }
}