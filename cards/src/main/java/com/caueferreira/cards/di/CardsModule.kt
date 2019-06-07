package com.caueferreira.cards.di

import com.caueferreira.cards.data.CardsRepository
import com.caueferreira.cards.data.network.CardsApi
import com.caueferreira.cards.data.network.CardsNetworkRepository
import com.caueferreira.cards.view.CardsDataSource
import com.caueferreira.cards.view.CardsDataSourceFactory
import com.caueferreira.cards.view.activity.CardsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val cardsModule = module {
    single { createApi<CardsApi>(get()) }
    single { provideCardsRepository(get()) }
    single { provideCardsDataSource(get()) }
    single { provideCardsDataSourceFactory(get()) }
    viewModel { CardsViewModel(get()) }
}

private fun provideCardsRepository(cardsApi: CardsApi): CardsRepository =
    CardsNetworkRepository(cardsApi)

private fun provideCardsDataSource(cardsRepository: CardsRepository): CardsDataSource = CardsDataSource(cardsRepository)

private fun provideCardsDataSourceFactory(dataSource: CardsDataSource): CardsDataSourceFactory =
    CardsDataSourceFactory(dataSource)

private inline fun <reified T> createApi(retrofit: Retrofit) = retrofit.create(T::class.java)