package com.libraryofalexandria.cards.view.expansions.transformers

import com.libraryofalexandria.cards.view.expansions.ExpansionViewModel
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ExpansionViewEntityMapperTest {

    @Mock
    private val filterMapper = ExpansionFilterViewEntityMapper()

    private lateinit var mapper: ExpansionViewEntityMapper

    @Before
    fun `before each`(){
        MockitoAnnotations.initMocks(this)

        mapper = ExpansionViewEntityMapper(filterMapper )
    }
}