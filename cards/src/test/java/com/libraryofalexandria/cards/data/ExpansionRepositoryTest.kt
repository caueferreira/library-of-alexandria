package com.libraryofalexandria.cards.data

import com.libraryofalexandria.cards.data.local.ExpansionsLocalDataSource
import com.libraryofalexandria.cards.data.network.ExpansionsRemoteDataSource
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ExpansionRepositoryTest() {

    @Mock
    private lateinit var remote: ExpansionsRemoteDataSource
    @Mock
    private lateinit var local: ExpansionsLocalDataSource

    private lateinit var repository: ExpansionRepository

    @Before
    fun `before each`() {
        MockitoAnnotations.initMocks(this)

        repository = ExpansionRepository(remote, local)
    }

    private inner class ExpansionRepositoryBuilder {
    }
}