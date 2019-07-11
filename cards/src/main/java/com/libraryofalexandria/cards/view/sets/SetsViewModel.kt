package com.libraryofalexandria.cards.view.sets

import android.view.View
import androidx.lifecycle.*
import com.libraryofalexandria.cards.data.FiltersRepository
import com.libraryofalexandria.cards.domain.FetchSets
import com.libraryofalexandria.cards.domain.Set
import com.libraryofalexandria.cards.domain.SetsResult
import com.libraryofalexandria.cards.view.sets.transformers.SetViewEntityMapper
import com.libraryofalexandria.cards.view.sets.ui.SetsViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.stream.Collectors.toList

class SetsViewModel(
    private val fetchSets: FetchSets,
    private val filterRepository: FiltersRepository,
    private val viewState: SetsViewState = SetsViewState(),
    private val mapper: SetViewEntityMapper = SetViewEntityMapper()
) : ViewModel() {

    private val _state by lazy { MutableLiveData<SetsViewState>() }
    val state: LiveData<SetsViewState> get() = _state

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        fetchSets.fetch()
            .collect {
                when (it) {
                    is SetsResult.Loading -> _state.value = viewState.copy(isLoading = View.VISIBLE)
                    is SetsResult.Success.Cache -> _state.value = showSets(it.result)
                    is SetsResult.Success.Network -> _state.value = showSets(it.result, true)
                    is SetsResult.Failure -> _state.value = viewState.copy(throwable = it.error, isError = View.VISIBLE)
                }
            }
    }


    val filters = liveData {
        filterRepository.get().collect {
            emit(it)
        }
    }

    private fun showSets(result: List<Set>, isUpdate: Boolean = false) =
        viewState.copy(isUpdate = isUpdate, isLoading = View.INVISIBLE, sets = result.stream()
            .map { mapper.transform(it) }
            .collect(toList())
        )

}