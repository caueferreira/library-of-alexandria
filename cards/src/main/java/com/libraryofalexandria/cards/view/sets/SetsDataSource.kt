package com.libraryofalexandria.cards.view.sets

import android.util.Log
import androidx.paging.ItemKeyedDataSource
import com.libraryofalexandria.cards.data.network.SetNetworkRepository
import com.libraryofalexandria.cards.view.State
import com.libraryofalexandria.cards.view.ViewState
import com.libraryofalexandria.cards.view.sets.transformers.SetViewEntityMapper
import com.libraryofalexandria.cards.view.sets.ui.SetViewEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class SetsDataSource(
    private val repository: SetNetworkRepository,
    private val mapper: SetViewEntityMapper = SetViewEntityMapper(),
    override val coroutineContext: CoroutineContext = Dispatchers.IO,
    private val viewState: ViewState = ViewState()
) : CoroutineScope, ItemKeyedDataSource<String, SetViewEntity>() {

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<SetViewEntity>) {
        launch {
            viewState.update(State.LOADING)

            repository.list().onSuccess {
                viewState.update(State.DONE)

                val sets = it
                    .map { mapper.transform(it) }
                    .toList()

                callback.onResult(
                    sets,
                    0, sets.size
                )
            }.onFailure {
                viewState.update(State.ERROR)
                Log.e("onFailure", "$it")
            }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<SetViewEntity>) {
        launch {
            viewState.update(State.LOADING)

            repository.list().onSuccess {
                viewState.update(State.DONE)

                val sets = it
                    .map { mapper.transform(it) }
                    .toList()

                callback.onResult(sets)
            }.onFailure {
                viewState.update(State.ERROR)
                Log.e("onFailure", "$it")
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<SetViewEntity>) {}

    override fun getKey(item: SetViewEntity) = item.id

    fun state() = viewState.state()
}