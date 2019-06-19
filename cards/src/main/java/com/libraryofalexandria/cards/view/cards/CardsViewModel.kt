package com.libraryofalexandria.cards.view.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.libraryofalexandria.cards.view.State
import com.libraryofalexandria.cards.view.cards.ui.CardViewEntity

private const val PAGE_SIZE = 1
private const val INITIAL_LOAD_SIZE_HINT = 25


class CardsViewModel(
    private val factory: CardsDataSourceFactory
) : ViewModel() {

    private var _cards: LiveData<PagedList<CardViewEntity>>

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPageSize(PAGE_SIZE)
            .build()

        _cards = LivePagedListBuilder<String, CardViewEntity>(factory, config).build()
    }

    fun cards() = _cards

    fun state(): LiveData<State> = Transformations.switchMap<CardsDataSource,
            State>(factory.liveData, CardsDataSource::state)
}