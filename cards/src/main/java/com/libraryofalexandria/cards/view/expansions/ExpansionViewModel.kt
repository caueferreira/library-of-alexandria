package com.libraryofalexandria.cards.view.expansions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.cards.domain.FetchExpansions
import com.libraryofalexandria.cards.domain.FetchFilters
import com.libraryofalexandria.cards.view.expansions.transformers.ExpansionViewEntityMapper
import com.libraryofalexandria.cards.view.expansions.ui.FilterViewEntity
import com.libraryofalexandria.core.base.Action
import com.libraryofalexandria.core.base.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ExpansionViewModel(
    private val fetchExpansions: FetchExpansions,
    private val fetchFilters: FetchFilters,
    private val mapper: ExpansionViewEntityMapper = ExpansionViewEntityMapper()
) : BaseViewModel() {

    private var state: MutableLiveData<ExpansionState> = MutableLiveData()
    fun state() = state

    init {
//        handleAction(ExpansionAction.FirstLoad)
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
        fetchFilters.fetch()
            .onEach { filtersState(it) }
            .launchIn(this)
    }

    private fun filtersState(list: List<FilterViewEntity>) {
        state.value = ExpansionState.Filters.Loaded(filters = list)
    }


    private fun errorState(error: Throwable) {
        state.value =
            ExpansionState.Expansions.Error.Generic(message = error.localizedMessage)
    }

    private fun expansionState(list: List<Expansion>) {
        if (list.isNotEmpty()) {
        state.value =
            ExpansionState.Expansions.Loaded(expansions = mapExpansions(list))
        }
    }

    private fun loadingState() {
        state.value = ExpansionState.Expansions.Loading()
    }

    private fun mapExpansions(expansions: List<Expansion>) = expansions.map {
        mapper.transform(it) }.toList()
}