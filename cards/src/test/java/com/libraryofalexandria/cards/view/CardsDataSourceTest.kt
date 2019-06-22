package com.libraryofalexandria.cards.view

import com.libraryofalexandria.cards.view.cards.transformer.CardViewEntityMapper
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CardsDataSourceTest {

    @Mock
    private lateinit var repository: Repository
    @Mock
    private lateinit var mapper: CardViewEntityMapper
    @Mock
    private lateinit var viewState: ViewState

    private lateinit var source: CardsDataSource

    @Before
    fun `before each`() {
        MockitoAnnotations.initMocks(this)

        source = CardsDataSource(repository, mapper, viewState = viewState)
    }

}