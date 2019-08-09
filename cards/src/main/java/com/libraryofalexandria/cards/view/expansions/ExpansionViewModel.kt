package com.libraryofalexandria.cards.view.expansions

import androidx.lifecycle.*
import com.libraryofalexandria.cards.data.FiltersRepository
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.cards.domain.FetchExpansions
import com.libraryofalexandria.cards.view.expansions.transformers.ExpansionViewEntityMapper
import com.libraryofalexandria.cards.view.expansions.ui.FilterViewEntity
import com.libraryofalexandria.core.base.Action
import com.libraryofalexandria.core.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

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
        loadingState()
        fetchExpansions.fetch()
            .onEach { expansionState(it) }
            .catch { errorState(it) }
            .launchIn(this)
    }

    private fun fetchFilters() = viewModelScope.launch {
        filterRepository.get().collect {
            filtersState(it)
        }
    }

    private fun filtersState(it: List<FilterViewEntity>) {
        _state.value = ExpansionState.Filters.Loaded(filters = it)
    }


    private fun errorState(error: Throwable) {
        _state.value =
            ExpansionState.Expansions.Error.Generic(message = error.localizedMessage)
    }

    private fun expansionState(list: List<Expansion>) {
        if (list.isNotEmpty()) {
            _state.value =
                ExpansionState.Expansions.Loaded(expansions = mapExpansions(list))
        }
    }

    private fun loadingState() {
        _state.value = ExpansionState.Expansions.Loading()
    }

    private fun mapExpansions(expansions: List<Expansion>) = expansions.map { mapper.transform(it) }.toList()
}