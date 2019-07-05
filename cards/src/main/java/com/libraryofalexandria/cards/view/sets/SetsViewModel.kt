package com.libraryofalexandria.cards.view.sets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libraryofalexandria.cards.data.FiltersRepository
import com.libraryofalexandria.cards.domain.FetchSets
import com.libraryofalexandria.cards.view.State
import com.libraryofalexandria.cards.view.sets.transformers.SetViewEntityMapper
import com.libraryofalexandria.cards.view.sets.ui.FilterViewEntity
import com.libraryofalexandria.cards.view.sets.ui.SetViewEntity
import com.libraryofalexandria.core.extensions.addOrRemove
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.stream.Collectors

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
                .filterNot { it.result.getOrNull() == null && it is FetchSets.FetchResult.Cache }
                .map { it.result }
                .map {
                    it.onSuccess {
                        _load = it.map {
                            mapper.transform(it)
                        }.toList()
                        _sets.value = _load
                        _state.value = State.DONE

                    }.onFailure {
                        _state.value = State.DONE
                    }
                }.collect { }
        }
    }

    private fun fetchFilters() {
        viewModelScope.launch {
            filterRepository.get()
                .onSuccess { filters ->
                    _filters.value = filters.toList()
                    _filterBy = filters.map { it }.toList().toMutableList()
                }
        }
    }

    fun filterBy(filter: FilterViewEntity) {
        _filterBy.addOrRemove(filter)
        _sets.value = _load.stream().filter { _filterBy.contains(it.filterViewEntity) }.collect(Collectors.toList())
    }
}