package com.libraryofalexandria.cards.view.sets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.libraryofalexandria.cards.data.network.SetNetworkRepository
import com.libraryofalexandria.cards.view.State
import com.libraryofalexandria.cards.view.sets.transformers.SetViewEntityMapper
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class SetsViewModel(
    private val repository: SetNetworkRepository,
    private val mapper: SetViewEntityMapper = SetViewEntityMapper()
) : ViewModel() {

    val sets = liveData {
        _state.value = State.LOADING
        repository.list()
            .onSuccess { sets ->
                _state.value = State.DONE
                emit(
                    sets.map { card ->
                        mapper.transform(card)
                    }.toList()
                )
            }.onFailure {
                _state.value = State.ERROR
            }
    }

    private val _state by lazy { MutableLiveData<State>() }
    val state: LiveData<State> get() = _state
}