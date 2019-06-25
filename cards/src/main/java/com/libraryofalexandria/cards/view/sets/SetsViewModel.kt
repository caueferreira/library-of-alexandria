package com.libraryofalexandria.cards.view.sets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libraryofalexandria.cards.data.network.SetNetworkRepository
import com.libraryofalexandria.cards.view.State
import com.libraryofalexandria.cards.view.sets.transformers.SetViewEntityMapper
import com.libraryofalexandria.cards.view.sets.ui.SetFilter
import com.libraryofalexandria.cards.view.sets.ui.SetViewEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.stream.Collectors

class SetsViewModel(
    private val repository: SetNetworkRepository,
    private val mapper: SetViewEntityMapper = SetViewEntityMapper()
) : ViewModel() {

    private var _load: List<SetViewEntity> = listOf()

    private val _sets by lazy { MutableLiveData<List<SetViewEntity>>() }
    val sets: LiveData<List<SetViewEntity>> get() = _sets

    private val _state by lazy { MutableLiveData<State>() }
    val state: LiveData<State> get() = _state

    fun fetch() {
        viewModelScope.launch {
            _state.value = State.LOADING
            repository.list()
                .onSuccess { sets ->
                    _state.value = State.DONE
                    _sets.value = sets.map { card ->
                        mapper.transform(card)
                    }.toList()

                    _load = _sets.value!!
                }.onFailure {
                    _state.value = State.ERROR
                }
        }
    }

    fun filter(filters: List<SetFilter>) {
        if (filters.isEmpty()) {
            _sets.value = _load
            return
        }

        _sets.value = _load.stream().filter { filters.contains(it.setFilter) }.collect(Collectors.toList())
    }
}