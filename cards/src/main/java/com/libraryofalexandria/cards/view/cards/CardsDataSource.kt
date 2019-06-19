package com.libraryofalexandria.cards.view.cards

import android.util.Log
import androidx.paging.ItemKeyedDataSource
import com.libraryofalexandria.cards.data.network.CardNetworkRepository
import com.libraryofalexandria.cards.view.State
import com.libraryofalexandria.cards.view.ViewState
import com.libraryofalexandria.cards.view.cards.transformer.CardViewEntityMapper
import com.libraryofalexandria.cards.view.cards.ui.CardViewEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CardsDataSource(
    private val repository: CardNetworkRepository,
    private val mapper: CardViewEntityMapper = CardViewEntityMapper(),
    override val coroutineContext: CoroutineContext = Dispatchers.IO,
    private val viewState: ViewState = ViewState()
) : CoroutineScope, ItemKeyedDataSource<String, CardViewEntity>() {

    private var page = 1

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<CardViewEntity>) {
        launch {
            viewState.update(State.LOADING)

            repository.list("INV", page).onSuccess {
                viewState.update(State.DONE)

                val cards = it
                    .map { mapper.transform(it) }
                    .toList()

                callback.onResult(
                    cards,
                    0, cards.size
                )
            }.onFailure {
                viewState.update(State.ERROR)
                Log.e("onFailure", "$it")
            }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<CardViewEntity>) {
        launch {
            viewState.update(State.LOADING)
            page++

            repository.list("INV",page).onSuccess {
                viewState.update(State.DONE)

                val cards = it
                    .map { mapper.transform(it) }
                    .toList()

                callback.onResult(cards)
            }.onFailure {
                viewState.update(State.ERROR)
                Log.e("onFailure", "$it")
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<CardViewEntity>) {}

    override fun getKey(item: CardViewEntity) = item.id

    fun state() = viewState.state()
}