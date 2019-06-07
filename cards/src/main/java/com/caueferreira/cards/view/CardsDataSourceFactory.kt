package com.caueferreira.cards.view

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.caueferreira.cards.view.activity.CardViewEntity

class CardsDataSourceFactory(
    private val dataSource: CardsDataSource
) : DataSource.Factory<String, CardViewEntity>() {

    val liveData = MutableLiveData<CardsDataSource>()

    override fun create(): DataSource<String, CardViewEntity> {
        liveData.postValue(dataSource)
        return dataSource
    }
}