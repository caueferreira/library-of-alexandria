package com.libraryofalexandria.cards.view.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libraryofalexandria.cards.data.network.CardNetworkRepository
import com.libraryofalexandria.cards.view.State
import com.libraryofalexandria.cards.view.cards.transformer.CardViewEntityMapper
import com.libraryofalexandria.cards.view.cards.ui.CardViewEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class CardsViewModel(
    private val repository: CardNetworkRepository,
    private val mapper: CardViewEntityMapper = CardViewEntityMapper()
) : ViewModel() {

    private var page = 0

    fun fetch(set: String) {
        viewModelScope.launch {
            page++
            _state.value = State.LOADING

            repository.list(set, page)
                .onSuccess { cards ->
                    _state.value = State.DONE
                    _cards.value =
                        cards.map { card ->
                            mapper.transform(card)
                        }.toList()
                }.onFailure {
                    _state.value = State.ERROR
                }
        }
    }

    private val _cards by lazy { MutableLiveData<List<CardViewEntity>>() }
    val cards: LiveData<List<CardViewEntity>> get() = _cards

    private val _state by lazy { MutableLiveData<State>() }
    val state: LiveData<State> get() = _state
}