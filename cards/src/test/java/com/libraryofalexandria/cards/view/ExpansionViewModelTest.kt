package com.libraryofalexandria.cards.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.libraryofalexandria.cards.data.FiltersRepository
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.cards.domain.FetchExpansions
import com.libraryofalexandria.cards.view.expansions.ExpansionAction
import com.libraryofalexandria.cards.view.expansions.ExpansionState
import com.libraryofalexandria.cards.view.expansions.ExpansionViewModel
import com.libraryofalexandria.cards.view.expansions.transformers.ExpansionViewEntityMapper
import com.libraryofalexandria.cards.view.expansions.ui.ExpansionViewEntity
import com.libraryofalexandria.cards.view.expansions.ui.FilterViewEntity
import com.libraryofalexandria.core.base.Action
import com.libraryofalexandria.core.base.Activities.Cards.expansion
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ExpansionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var fetchExpansions: FetchExpansions
    @Mock
    private lateinit var filterRepository: FiltersRepository
    @Mock
    private lateinit var mapper: ExpansionViewEntityMapper

    @Mock
    lateinit var observer: Observer<ExpansionState>

    private lateinit var viewModel: ExpansionViewModel

    @Before
    fun `before each`() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockitoAnnotations.initMocks(this)

        viewModel = ExpansionViewModel(fetchExpansions, filterRepository, mapper)
    }

    @After
    fun `after each`() {
        Dispatchers.resetMain()
    }

    private val expansion = mock<Expansion> { Expansion::class }
    private val viewEntity = mock<ExpansionViewEntity> { ExpansionViewEntity::class }

    private val filter = mock<FilterViewEntity> { FilterViewEntity::class }

    @Test
    fun `FirstLoad Action should return states then FiltersLoaded`() {
        viewModel.state().observeForever(observer)

        runBlocking {
            ExpansionViewModelBuilder()
                .withExpansions(arrayListOf(expansion))
                .withFilters(arrayListOf(filter))
                .action(ExpansionAction.FirstLoad)
        }

        verify(observer, times(1)).onChanged(ExpansionState.Filters.Loaded(filters = arrayListOf(filter)))
        verify(observer, times(1)).onChanged(ExpansionState.Expansions.Loading())
        verify(observer, times(1)).onChanged(ExpansionState.Expansions.Loaded(expansions = arrayListOf(viewEntity)))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun `LoadExpansion Action should return states ExpansionsLoading then ExpansionsLoaded`() {
        viewModel.state().observeForever(observer)

        runBlocking {
            ExpansionViewModelBuilder()
                .withExpansions(arrayListOf(expansion))
                .action(ExpansionAction.LoadExpansion)
        }

        verify(observer, times(1)).onChanged(ExpansionState.Expansions.Loading())
        verify(observer, times(1)).onChanged(ExpansionState.Expansions.Loaded(expansions = arrayListOf(viewEntity)))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun `LoadFilters Action should return states then FiltersLoaded`() {
        viewModel.state().observeForever(observer)

        runBlocking {
            ExpansionViewModelBuilder()
                .withFilters(arrayListOf(filter))
                .action(ExpansionAction.LoadFilters)
        }

        verify(observer, times(1)).onChanged(ExpansionState.Filters.Loaded(filters = arrayListOf(filter)))
        verifyNoMoreInteractions(observer)
    }

    private inner class ExpansionViewModelBuilder {

        suspend fun withFilters(list: List<FilterViewEntity>): ExpansionViewModelBuilder {
            whenever(filterRepository.get()).thenReturn(flowOf(list))
            return this
        }

        suspend fun withExpansions(list: List<Expansion>): ExpansionViewModelBuilder {
            whenever(fetchExpansions.fetch()).thenReturn(flowOf(list))
            if (list.isNotEmpty()) {
                whenever(mapper.transform(any())).thenReturn(viewEntity)
            }
            return this
        }

        fun action(action: Action) = viewModel.handleAction(action)
    }
}