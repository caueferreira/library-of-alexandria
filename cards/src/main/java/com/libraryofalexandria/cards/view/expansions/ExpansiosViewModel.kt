package com.libraryofalexandria.cards.view.expansions

import android.view.View
import androidx.lifecycle.*
import com.libraryofalexandria.cards.data.FiltersRepository
import com.libraryofalexandria.cards.domain.FetchExpansions
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.cards.domain.ExpansionResult
import com.libraryofalexandria.cards.view.expansions.transformers.ExpansionViewEntityMapper
import com.libraryofalexandria.cards.view.expansions.ui.ExpansionViewState
import com.libraryofalexandria.core.base.Action
import com.libraryofalexandria.core.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExpansiosViewModel(
    private val fetchExpansions: FetchExpansions,
    private val filterRepository: FiltersRepository,
    private val mapper: ExpansionViewEntityMapper = ExpansionViewEntityMapper()
) : BaseViewModel() {

    private val _state by lazy { MutableLiveData<ExpansionViewState>() }
    val state: LiveData<ExpansionViewState> get() = _state

    init {
        handleAction(ExpansionAction.FirstLoad)
    }

    override fun handleAction(action: Action) {
        when (action) {
            is ExpansionAction.FirstLoad -> fetchExpansions()
            is ExpansionAction.LoadExpansion -> fetchExpansions()
        }
    }


    private fun fetchExpansions() = viewModelScope.launch {
        fetchExpansions.fetch()
            .collect {
                _state.value = ExpansionViewState.Expansions.Loading()
                when (it) {
                    is ExpansionResult.Success.Cache -> _state.value =
                        ExpansionViewState.Expansions.Loaded(expansions = mapExpansions(it.result))
                    is ExpansionResult.Success.Network -> _state.value =
                        ExpansionViewState.Expansions.Loaded(
                            isUpdate = it.isUpdate,
                            expansions = mapExpansions(it.result)
                        )
                    is ExpansionResult.Failure -> _state.value =
                        ExpansionViewState.Expansions.Error.Generic(message = it.error.localizedMessage)
                }
            }
    }

    val filters = liveData {
        filterRepository.get().collect {
            emit(it)
        }
    }

    private fun mapExpansions(expansions: List<Expansion>) = expansions.map { mapper.transform(it) }.toList()
}