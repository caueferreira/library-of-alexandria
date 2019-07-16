package com.libraryofalexandria.cards.view.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.libraryofalexandria.cards.domain.*
import com.libraryofalexandria.cards.view.cards.transformer.CardViewEntityMapper
import com.libraryofalexandria.core.base.Action
import com.libraryofalexandria.core.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CardsViewModel(
    private val fetchCards: FetchCards,
    private val mapper: CardViewEntityMapper = CardViewEntityMapper()
) : BaseViewModel() {

    private val _state by lazy { MutableLiveData<CardState>() }
    val state: LiveData<CardState> get() = _state

    private var page = 1
    private var expansion = ""

    override fun handleAction(action: Action) {
        when (action) {
            is CardAction.FirstLoad -> {
                expansion = action.expansion
                fetchCards(action.expansion, page)
            }

            is CardAction.LoadMore -> {
                page++
                fetchCards(expansion, page)
            }
        }
    }

    private fun fetchCards(expansion: String, page: Int) = viewModelScope.launch {
        loadingState()
        fetchCards.fetch(expansion, page)
            .collect {
                when (it) {
                    is CardResult.Success.Network -> cardsState(it)
                    is CardResult.Failure -> errorState(it)
                }
            }
    }

    private fun cardsState(it: CardResult.Success.Network) {
        _state.value =
            CardState.Loaded(
                cards = mapCards(it.result)
            )
    }

    private fun errorState(it: CardResult.Failure) {
        _state.value =
            CardState.Error.Generic(message = it.error.localizedMessage)
    }

    private fun loadingState() {
        _state.value = CardState.Loading()
    }

    private fun mapCards(cards: List<Card>) = cards.map { mapper.transform(it) }.toList()
}