package com.libraryofalexandria.cards.view.expansions

import androidx.lifecycle.*
import com.libraryofalexandria.cards.data.FiltersRepository
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.cards.domain.FetchExpansions
import com.libraryofalexandria.cards.domain.ExpansionResult
import com.libraryofalexandria.cards.view.expansions.transformers.ExpansionViewEntityMapper
import com.libraryofalexandria.cards.view.expansions.ui.FilterViewEntity
import com.libraryofalexandria.core.base.Action
import com.libraryofalexandria.core.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExpansionViewModel(
    private val fetchExpansions: FetchExpansions,
    private val filterRepository: FiltersRepository,
    private val mapper: ExpansionViewEntityMapper = ExpansionViewEntityMapper()
) : BaseViewModel() {

    private val _state by lazy { MutableLiveData<ExpansionState>() }
    val state: LiveData<ExpansionState> get() = _state

    init {
        handleAction(ExpansionAction.FirstLoad)
    }

    override fun handleAction(action: Action) {
        when (action) {
            is ExpansionAction.FirstLoad -> {
                fetchFilters()
                fetchExpansions()
            }
            is ExpansionAction.LoadExpansion -> fetchExpansions()
            is ExpansionAction.LoadFilters -> fetchFilters()
        }
    }

    private fun fetchExpansions() = viewModelScope.launch {
        fetchExpansions.fetch()
            .collect {
                loadingState()
                when (it) {
                    is ExpansionResult.Success.Cache -> expansionCacheState(it)
                    is ExpansionResult.Success.Network -> expansionNetworkState(it)
                    is ExpansionResult.Failure -> errorState(it)
                }
            }
    }

    private fun fetchFilters() = viewModelScope.launch {
        filterRepository.get().collect {
            filtersState(it)
        }
    }

    private fun filtersState(it: List<FilterViewEntity>) {
        _state.value = ExpansionState.Filters.Loaded(filters = it)
    }


    private fun errorState(it: ExpansionResult.Failure) {
        _state.value =
            ExpansionState.Expansions.Error.Generic(message = it.error.localizedMessage)
    }

    private fun expansionNetworkState(it: ExpansionResult.Success.Network) {
        _state.value =
            ExpansionState.Expansions.Loaded(
                isUpdate = it.isUpdate,
                expansions = mapExpansions(it.result)
            )
    }

    private fun expansionCacheState(it: ExpansionResult.Success.Cache) {
        _state.value =
            ExpansionState.Expansions.Loaded(expansions = mapExpansions(it.result))
    }

    private fun loadingState() {
        _state.value = ExpansionState.Expansions.Loading()
    }

    private fun mapExpansions(expansions: List<Expansion>) = expansions.map { mapper.transform(it) }.toList()
}