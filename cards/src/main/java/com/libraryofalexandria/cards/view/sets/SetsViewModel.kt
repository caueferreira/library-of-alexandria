package com.libraryofalexandria.cards.view.sets

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.libraryofalexandria.cards.view.State
import com.libraryofalexandria.cards.view.sets.ui.SetViewEntity

private const val PAGE_SIZE = 1
private const val INITIAL_LOAD_SIZE_HINT = 25


class SetsViewModel(
    private val factory: SetsDataSourceFactory
) : ViewModel() {

    private var _sets: LiveData<PagedList<SetViewEntity>>

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPageSize(PAGE_SIZE)
            .build()

        _sets = LivePagedListBuilder<String, SetViewEntity>(factory, config).build()
    }

    fun sets() = _sets

    fun state(): LiveData<State> = Transformations.switchMap<SetsDataSource,
            State>(factory.liveData, SetsDataSource::state)
}