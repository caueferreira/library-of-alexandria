package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.transformer.CardMapper
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

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
        val response = RootResponse(arrayListOf())
        whenever(api.get(0)).thenReturn(GlobalScope.async { response })

        runBlocking {
            repository.get(0).onSuccess {
                assertEquals(0, it.size)
            }
        }

        verify(api, times(1)).get(any())
        verifyZeroInteractions(mapper)
    }

    @Test
    fun `should return empty because none was english`() {
        val card = mock<CardResponse> { CardResponse::class }
        val response = RootResponse(arrayListOf(card))

        whenever(api.get(0)).thenReturn(GlobalScope.async { response })
        whenever(card.language).thenReturn("pt")

        runBlocking {
            repository.get(0).onSuccess {
                assertEquals(0, it.size)
            }
        }

        verify(api, times(1)).get(any())
        verifyZeroInteractions(mapper)
    }
}