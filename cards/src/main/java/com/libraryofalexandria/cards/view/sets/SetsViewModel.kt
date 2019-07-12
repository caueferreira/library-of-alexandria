package com.libraryofalexandria.cards.view.sets

import android.view.View
import androidx.lifecycle.*
import com.libraryofalexandria.cards.data.FiltersRepository
import com.libraryofalexandria.cards.domain.FetchSets
import com.libraryofalexandria.cards.domain.Set
import com.libraryofalexandria.cards.domain.SetsResult
import com.libraryofalexandria.cards.view.sets.transformers.SetViewEntityMapper
import com.libraryofalexandria.cards.view.sets.ui.SetsViewState
import com.libraryofalexandria.core.Action
import com.libraryofalexandria.core.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SetsViewModel(
    private val fetchSets: FetchSets,
    private val filterRepository: FiltersRepository,
    private val mapper: SetViewEntityMapper = SetViewEntityMapper()
) : BaseViewModel() {

    private val _state by lazy { MutableLiveData<SetsViewState>() }
    val state: LiveData<SetsViewState> get() = _state

    init {
        handleAction(SetsAction.FirstLoad)
    }

    override fun handleAction(action: Action) {
        when (action) {
            is SetsAction.FirstLoad -> fetchSets()
            is SetsAction.LoadSets -> fetchSets()
        }
    }


    private fun fetchSets() = viewModelScope.launch {
        fetchSets.fetch()
            .collect {
                _state.value = SetsViewState.Sets.Loading()
                when (it) {
                    is SetsResult.Success.Cache -> _state.value =
                        SetsViewState.Sets.SetsLoaded(sets = mapSets(it.result))
                    is SetsResult.Success.Network -> _state.value =
                        SetsViewState.Sets.SetsLoaded(sets = mapSets(it.result))
                    is SetsResult.Failure -> _state.value =
                        SetsViewState.Sets.Error.Generic(message = it.error.localizedMessage)
                }
            }
    }

    var hasEmited = false

    private fun emitSets(){

    }


    val filters = liveData {
        filterRepository.get().collect {
            emit(it)
        }
    }

    private fun mapSets(sets: List<Set>) = sets.map { mapper.transform(it) }.toList()
}