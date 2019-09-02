package com.libraryofalexandria.cards.view.expansions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.cards.domain.FetchExpansions
import com.libraryofalexandria.cards.domain.FetchFilters
import com.libraryofalexandria.cards.domain.Filters
import com.libraryofalexandria.cards.view.expansions.transformers.ExpansionFilterViewEntityMapper
import com.libraryofalexandria.cards.view.expansions.transformers.ExpansionViewEntityMapper
import com.libraryofalexandria.cards.view.expansions.ui.ExpansionViewEntity
import com.libraryofalexandria.cards.view.expansions.ui.FilterViewEntity
import com.libraryofalexandria.core.base.Action
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
    private lateinit var fetchFilters: FetchFilters
    @Mock
    private lateinit var expansionMapper: ExpansionViewEntityMapper
    @Mock
    private lateinit var filterMapper: ExpansionFilterViewEntityMapper

    @Mock
    lateinit var observer: Observer<ExpansionState>

    private lateinit var viewModel: ExpansionViewModel

    @Before
    fun `before each`() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockitoAnnotations.initMocks(this)

        viewModel = ExpansionViewModel(fetchExpansions, fetchFilters, expansionMapper, filterMapper)
    }

    @After
    fun `after each`() {
        Dispatchers.resetMain()
    }

    private val expansion = mock<Expansion> { Expansion::class }
    private val expansionViewEntity = mock<ExpansionViewEntity> { ExpansionViewEntity::class }

    private val filter = mock<Filters.Expansion> { Filters.Expansion::class }
    private val filterViewEntity = mock<FilterViewEntity> { FilterViewEntity::class }

    @Test
    fun `FirstLoad Action should return states then FiltersLoaded`() {
        viewModel.state().observeForever(observer)

        runBlocking {
            ExpansionViewModelBuilder()
                .withExpansions(arrayListOf(expansion))
                .withFilters(arrayListOf(filter))
                .action(ExpansionAction.FirstLoad)
        }

        verify(filterMapper, atLeastOnce()).transform(any())
        verify(expansionMapper, atLeastOnce()).transform(any())
        verify(observer, times(1)).onChanged(ExpansionState.Filters.Loaded(filters = arrayListOf(filterViewEntity)))
        verify(observer, times(1)).onChanged(ExpansionState.Expansions.Loading())
        verify(observer, times(1)).onChanged(ExpansionState.Expansions.Loaded(expansions = arrayListOf(expansionViewEntity)))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun `FirstLoad Action should return states then FiltersLoaded but not Expansions due it being empty`() {
        viewModel.state().observeForever(observer)

        runBlocking {
            ExpansionViewModelBuilder()
                .withExpansions(arrayListOf())
                .withFilters(arrayListOf(filter))
                .action(ExpansionAction.FirstLoad)
        }

        verifyZeroInteractions(expansionMapper)
        verify(filterMapper, atLeastOnce()).transform(any())
        verify(observer, times(1)).onChanged(ExpansionState.Filters.Loaded(filters = arrayListOf(filterViewEntity)))
        verify(observer, times(1)).onChanged(ExpansionState.Expansions.Loading())
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

        verifyZeroInteractions(filterMapper)
        verify(expansionMapper, atLeastOnce()).transform(any())
        verify(observer, times(1)).onChanged(ExpansionState.Expansions.Loading())
        verify(observer, times(1)).onChanged(ExpansionState.Expansions.Loaded(expansions = arrayListOf(expansionViewEntity)))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun `LoadExpansion Action should return states ExpansionsLoading but not Expansions due it being empty`() {
        viewModel.state().observeForever(observer)

        runBlocking {
            ExpansionViewModelBuilder()
                .withExpansions(arrayListOf())
                .action(ExpansionAction.LoadExpansion)
        }

        verifyZeroInteractions(filterMapper)
        verifyZeroInteractions(expansionMapper)
        verify(observer, times(1)).onChanged(ExpansionState.Expansions.Loading())
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

        verifyZeroInteractions(expansionMapper)
        verify(filterMapper, atLeastOnce()).transform(any())
        verify(observer, times(1)).onChanged(ExpansionState.Filters.Loaded(filters = arrayListOf(filterViewEntity)))
        verifyNoMoreInteractions(observer)
    }

    private inner class ExpansionViewModelBuilder {

        suspend fun withFilters(list: List<Filters.Expansion>): ExpansionViewModelBuilder {
            whenever(fetchFilters.fetch()).thenReturn(flowOf(list))
            if(list.isNotEmpty()){
                whenever(filterMapper.transform(any())).thenReturn(filterViewEntity)
            }
            return this
        }

        suspend fun withExpansions(list: List<Expansion>): ExpansionViewModelBuilder {
            whenever(fetchExpansions.fetch()).thenReturn(flowOf(list))
            if (list.isNotEmpty()) {
                whenever(expansionMapper.transform(any())).thenReturn(expansionViewEntity)
            }
            return this
        }

        fun action(action: Action) = viewModel.handleAction(action)
    }
}