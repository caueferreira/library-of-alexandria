package com.libraryofalexandria.cards.view.sets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libraryofalexandria.cards.data.FiltersRepository
import com.libraryofalexandria.cards.domain.FetchSets
import com.libraryofalexandria.cards.domain.Set
import com.libraryofalexandria.cards.view.State
import com.libraryofalexandria.cards.view.sets.transformers.SetViewEntityMapper
import com.libraryofalexandria.cards.view.sets.ui.FilterViewEntity
import com.libraryofalexandria.cards.view.sets.ui.SetViewEntity
import com.libraryofalexandria.core.ViewState
import com.libraryofalexandria.core.extensions.addOrRemove
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.stream.Collectors.toList

class SetsViewModel(
    private val fetchSets: FetchSets,
    private val filterRepository: FiltersRepository,
    private val mapper: SetViewEntityMapper = SetViewEntityMapper()
) : ViewModel() {

    private var _load: List<SetViewEntity> = listOf()
    private var _filterBy: MutableList<FilterViewEntity> = mutableListOf()

    private val _sets by lazy { MutableLiveData<List<SetViewEntity>>() }
    val sets: LiveData<List<SetViewEntity>> get() = _sets

    private val _state by lazy { MutableLiveData<State>() }
    val state: LiveData<State> get() = _state

    private val _filters by lazy { MutableLiveData<List<FilterViewEntity>>() }
    val filters: LiveData<List<FilterViewEntity>> get() = _filters

    init {
        fetchSets()
        fetchFilters()
    }

    private fun fetchSets() {
        viewModelScope.launch {
            _state.value = State.LOADING
            fetchSets.fetch()
                .filterNot { it.result == null && it is FetchSets.FetchResult.Cache }
                .collect {
                    when (it) {
                        is FetchSets.FetchResult.Cache -> showSets(it.result)
                        is FetchSets.FetchResult.Update -> showSets(it.result)
                    }
                }
        }
    }

    private fun showSets(result: List<Set>) {
        _load = result.stream()
            .map { mapper.transform(it) }
            .collect(toList())
        _sets.value = _load
        _state.value = State.DONE
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
        _sets.value = _load.stream().filter { _filterBy.contains(it.filterViewEntity) }.collect(toList())
    }
}