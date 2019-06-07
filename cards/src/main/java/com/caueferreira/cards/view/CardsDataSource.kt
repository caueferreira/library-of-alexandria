package com.caueferreira.cards.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.caueferreira.cards.data.CardsRepository
import com.caueferreira.cards.view.activity.CardViewEntity
import com.caueferreira.cards.view.transformer.CardViewEntityMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CardsDataSource(
    private val repository: CardsRepository,
    private val mapper: CardViewEntityMapper = CardViewEntityMapper(),
    override val coroutineContext: CoroutineContext = Dispatchers.Main
) : CoroutineScope, ItemKeyedDataSource<String, CardViewEntity>() {

    private val jobs = ArrayList<Job>()
    infix private fun ArrayList<Job>.add(job: Job) {
        this.add(job)
    }

    var state: MutableLiveData<State> = MutableLiveData()
    private var page = 1

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<CardViewEntity>) {

        jobs add launch {
            updateState(State.LOADING)
            repository.get(page).onSuccess {
                updateState(State.DONE)
                val cards = it.map { mapper.transform(it) }
                callback.onResult(cards,
                    0, cards.size)
            }.onFailure {
                updateState(State.ERROR)
                Log.e("onFailure", "$it")
            }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<CardViewEntity>) {
        jobs add launch {
            updateState(State.LOADING)
            page++

            repository.get(page).onSuccess {
                updateState(State.DONE)
                val cards = it.map { mapper.transform(it) }
                callback.onResult(cards)
            }.onFailure {
                updateState(State.ERROR)
                Log.e("onFailure", "$it")
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<CardViewEntity>) {

    }

    override fun getKey(item: CardViewEntity) = item.id

    private fun updateState(state: State) {
        this.state.postValue(state)
    }
}