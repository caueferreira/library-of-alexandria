package com.libraryofalexandria.cards.view.sets

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.libraryofalexandria.cards.view.sets.ui.SetViewEntity

class SetsDataSourceFactory(
    private val dataSource: SetsDataSource
) : DataSource.Factory<String, SetViewEntity>() {

    val liveData = MutableLiveData<SetsDataSource>()

    override fun create(): DataSource<String, SetViewEntity> {
        liveData.postValue(dataSource)
        return dataSource
    }
}