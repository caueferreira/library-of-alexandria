package com.libraryofalexandria.cards.di

import com.libraryofalexandria.cards.data.network.CardNetworkRepository
import com.libraryofalexandria.cards.data.network.ScryfallApi
import com.libraryofalexandria.cards.data.network.SetNetworkRepository
import com.libraryofalexandria.cards.view.cards.CardsDataSource
import com.libraryofalexandria.cards.view.cards.CardsDataSourceFactory
import com.libraryofalexandria.cards.view.cards.CardsViewModel
import com.libraryofalexandria.cards.view.sets.SetsDataSource
import com.libraryofalexandria.cards.view.sets.SetsDataSourceFactory
import com.libraryofalexandria.cards.view.sets.SetsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val cardsModule = module {
    single { createApi<ScryfallApi>(get()) }

    single { provideCardsRepository(get()) }
    single { provideCardsDataSource(get()) }
    single { provideCardsDataSourceFactory(get()) }
    viewModel { CardsViewModel(get()) }

    single { provideSetsRepository(get()) }
    single { provideSetsDataSource(get()) }
    single { provideSetsDataSourceFactory(get()) }
    viewModel { SetsViewModel(get()) }
}

private fun provideCardsRepository(scryfallApi: ScryfallApi): CardNetworkRepository =
    CardNetworkRepository(scryfallApi)

private fun provideCardsDataSource(repository: CardNetworkRepository): CardsDataSource =
    CardsDataSource(repository)

private fun provideCardsDataSourceFactory(dataSource: CardsDataSource): CardsDataSourceFactory =
    CardsDataSourceFactory(dataSource)

private fun provideSetsRepository(scryfallApi: ScryfallApi): SetNetworkRepository =
    SetNetworkRepository(scryfallApi)

private fun provideSetsDataSource(repository: SetNetworkRepository): SetsDataSource =
    SetsDataSource(repository)

private fun provideSetsDataSourceFactory(dataSource: SetsDataSource): SetsDataSourceFactory =
    SetsDataSourceFactory(dataSource)

private inline fun <reified T> createApi(retrofit: Retrofit) = retrofit.create(T::class.java)