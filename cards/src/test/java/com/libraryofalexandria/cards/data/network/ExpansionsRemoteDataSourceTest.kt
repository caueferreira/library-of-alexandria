package com.libraryofalexandria.cards.data.network

import com.libraryofalexandria.cards.data.network.entity.ExpansionResponse
import com.libraryofalexandria.cards.data.network.entity.RootResponse
import com.libraryofalexandria.cards.data.transformer.ExpansionMapper
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.network.exception.NetworkError
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response

class ExpansionsRemoteDataSourceTest {

    @Mock
    private lateinit var api: ScryfallApi
    @Mock
    private lateinit var mapper: ExpansionMapper

    private lateinit var dataSource: ExpansionsRemoteDataSource

    private val emptyResponse = RootResponse<ExpansionResponse>(arrayListOf())

    @Before
    fun `before each`() {
        MockitoAnnotations.initMocks(this)

        dataSource = ExpansionsRemoteDataSource(api, mapper)
    }

    @Test
    fun `should return empty`() {
        runBlocking {
            val response = ExpansionsRemoteDataSourceBuilder()
                .withSource(emptyResponse)
                .list()

            Assert.assertEquals(0, response.count())
            verify(api, times(1)).expansions()
            verifyZeroInteractions(mapper)
        }
    }

    @Test
    fun `should return list of mapped expansions`() {
        runBlocking {
            val expansion = mock<ExpansionResponse> { ExpansionResponse::class }

            val response = ExpansionsRemoteDataSourceBuilder()
                .withSource(RootResponse(arrayListOf(expansion, expansion, expansion, expansion, expansion)))
                .list()


            val total = response.onEach { expansion ->
                Assert.assertEquals(Expansion::class, expansion::class)
            }.count()

            Assert.assertEquals(5, total)

            verify(api, times(1)).expansions()
            verify(mapper, times(5)).transform(any())
        }
    }

    @Test(expected = NetworkError.Http.NotFound::class)
    fun `should propagate http network error`() {
        runBlocking {
            ExpansionsRemoteDataSourceBuilder()
                .exception()
                .list()
        }
    }

    private inner class ExpansionsRemoteDataSourceBuilder {

        suspend fun withSource(response: RootResponse<ExpansionResponse>): ExpansionsRemoteDataSourceBuilder {
            whenever(api.expansions()).thenReturn(response)
            if (response.data.isNotEmpty()) {
                whenever(mapper.transform(any())).thenReturn(mock { Expansion::class })
            }

            return this
        }

        suspend fun list() = dataSource.list()

        suspend fun exception(): ExpansionsRemoteDataSourceBuilder {
            whenever(api.expansions()).thenThrow(httpException("Not Found", 404))
            return this
        }

        private fun httpException(message: String, statusCode: Int): HttpException {
            val apiMessage = """{"code":$statusCode ,"message": "$message"}"""
            val jsonMediaType = MediaType.parse("application/json")
            val body = ResponseBody.create(jsonMediaType, apiMessage)
            return HttpException(Response.error<Any>(statusCode, body))
        }
    }
}