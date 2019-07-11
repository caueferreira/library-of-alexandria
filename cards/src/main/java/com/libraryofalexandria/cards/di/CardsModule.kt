package com.libraryofalexandria.cards.di

import android.content.Context
import com.libraryofalexandria.cards.data.FiltersRepository
import com.libraryofalexandria.cards.data.local.FiltersLocalDataSource
import com.libraryofalexandria.cards.data.local.SetsLocalDataSource
import com.libraryofalexandria.cards.data.network.CardNetworkRepository
import com.libraryofalexandria.cards.data.network.ScryfallApi
import com.libraryofalexandria.cards.data.network.SetsRemoteDataSource
import com.libraryofalexandria.cards.domain.FetchSets
import com.libraryofalexandria.cards.view.cards.CardsViewModel
import com.libraryofalexandria.cards.view.sets.SetsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val cardsModule = module {
    single { createApi<ScryfallApi>(get()) }

    single { provideFiltersLocalDataSource() }
    single { provideFiltersRepository(get()) }

    single { provideCardsRepository(get()) }
    viewModel { CardsViewModel(get()) }

    single { provideSetsLocalDataSource(get()) }
    single { provideSetsRemoteDataSource(get()) }
    single { provideSetsUseCase(get(), get()) }
    viewModel { SetsViewModel(get(), get()) }
}

private fun provideCardsRepository(scryfallApi: ScryfallApi): CardNetworkRepository =
    CardNetworkRepository(scryfallApi)

private fun provideSetsLocalDataSource(context: Context): SetsLocalDataSource =
    SetsLocalDataSource(context)

private fun provideSetsRemoteDataSource(scryfallApi: ScryfallApi): SetsRemoteDataSource =
    SetsRemoteDataSource(scryfallApi)

private fun provideSetsUseCase(
    setsRemoteDataSource: SetsRemoteDataSource,
    localDataSource: SetsLocalDataSource
): FetchSets = FetchSets(setsRemoteDataSource, localDataSource)

private fun provideFiltersLocalDataSource(): FiltersLocalDataSource =
    FiltersLocalDataSource()

private fun provideFiltersRepository(localDataSource: FiltersLocalDataSource): FiltersRepository =
    FiltersRepository(localDataSource)

private inline fun <reified T> createApi(retrofit: Retrofit) = retrofit.create(T::class.java)