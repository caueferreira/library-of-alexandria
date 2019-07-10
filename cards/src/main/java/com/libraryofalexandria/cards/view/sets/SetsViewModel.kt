package com.libraryofalexandria.cards.view.sets

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libraryofalexandria.cards.data.FiltersRepository
import com.libraryofalexandria.cards.domain.FetchSets
import com.libraryofalexandria.cards.domain.Set
import com.libraryofalexandria.cards.domain.SetsResult
import com.libraryofalexandria.cards.view.sets.transformers.SetViewEntityMapper
import com.libraryofalexandria.cards.view.sets.ui.FilterViewEntity
import com.libraryofalexandria.cards.view.sets.ui.SetViewEntity
import com.libraryofalexandria.cards.view.sets.ui.SetsViewState
import com.libraryofalexandria.core.extensions.addOrRemove
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.stream.Collectors.toList

class SetsViewModel(
    private val fetchSets: FetchSets,
    private val filterRepository: FiltersRepository,
    private val viewState: SetsViewState = SetsViewState(),
    private val mapper: SetViewEntityMapper = SetViewEntityMapper()
) : ViewModel() {

    private var _load: List<SetViewEntity> = listOf()
    private var _filterBy: MutableList<FilterViewEntity> = mutableListOf()

    private val _state by lazy { MutableLiveData<SetsViewState>() }
    val state: LiveData<SetsViewState> get() = _state

    private val _filters by lazy { MutableLiveData<List<FilterViewEntity>>() }
    val filters: LiveData<List<FilterViewEntity>> get() = _filters

    init {
        fetchSets()
        fetchFilters()
    }

    private fun fetchSets() {
        viewModelScope.launch {
            fetchSets.fetch()
                .collect {
                    when (it) {
                        is SetsResult.Loading -> _state.value = viewState.copy(isLoading = View.VISIBLE)
                        is SetsResult.Success.Cache -> showSets(it.result)
                        is SetsResult.Success.Network -> showSets(it.result)
                    }
                }
        }
    }

    private fun showSets(result: List<Set>) {
        _load = result.stream()
            .map { mapper.transform(it) }
            .collect(toList())

        _state.value = viewState.copy(isLoading = View.INVISIBLE, sets = _load)
    }

    private fun fetchFilters() {
        viewModelScope.launch {
            filterRepository.get()
                .collect { filters ->
                    _filters.value = filters.toList()
                    _filterBy = filters.map { it }.toList().toMutableList()
                }
        }
    }

    fun filterBy(filter: FilterViewEntity) {
        _filterBy.addOrRemove(filter)
        _state.value = viewState.copy(
            isLoading = View.INVISIBLE,
            sets = _load.stream().filter { _filterBy.contains(it.filterViewEntity) }.collect(toList())
        )
    }
}