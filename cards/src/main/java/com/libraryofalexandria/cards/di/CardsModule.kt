package com.libraryofalexandria.cards.di

import android.content.Context
import com.libraryofalexandria.cards.data.FiltersRepository
import com.libraryofalexandria.cards.data.local.FiltersLocalDataSource
import com.libraryofalexandria.cards.data.local.ExpansionsLocalDataSource
import com.libraryofalexandria.cards.data.network.CardsRemoteDataSource
import com.libraryofalexandria.cards.data.network.ScryfallApi
import com.libraryofalexandria.cards.data.network.ExpansionsRemoteDataSource
import com.libraryofalexandria.cards.domain.FetchCards
import com.libraryofalexandria.cards.domain.FetchExpansions
import com.libraryofalexandria.cards.view.cards.CardsViewModel
import com.libraryofalexandria.cards.view.expansions.ExpansionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val cardsModule = module {
    single { createApi<ScryfallApi>(get()) }

    single { provideFiltersLocalDataSource() }
    single { provideFiltersRepository(get()) }

    single { provideCardsRemoteDataSource(get()) }
    single { provideCardsUseCase(get()) }
    viewModel { CardsViewModel(get()) }

    single { provideExpansionsLocalDataSource(get()) }
    single { provideExpansionsRemoteDataSource(get()) }
    single { provideExpansionsUseCase(get(), get()) }
    viewModel { ExpansionViewModel(get(), get()) }
}

private fun provideCardsRemoteDataSource(scryfallApi: ScryfallApi): CardsRemoteDataSource =
    CardsRemoteDataSource(scryfallApi)

private fun provideCardsUseCase(remoteDataSource: CardsRemoteDataSource) =
    FetchCards(remoteDataSource)

private fun provideExpansionsLocalDataSource(context: Context): ExpansionsLocalDataSource =
    ExpansionsLocalDataSource(context)

private fun provideExpansionsRemoteDataSource(scryfallApi: ScryfallApi): ExpansionsRemoteDataSource =
    ExpansionsRemoteDataSource(scryfallApi)

private fun provideExpansionsUseCase(
    remoteDataSource: ExpansionsRemoteDataSource,
    localDataSource: ExpansionsLocalDataSource
): FetchExpansions = FetchExpansions(remoteDataSource, localDataSource)

private fun provideFiltersLocalDataSource(): FiltersLocalDataSource =
    FiltersLocalDataSource()

private fun provideFiltersRepository(localDataSource: FiltersLocalDataSource): FiltersRepository =
    FiltersRepository(localDataSource)

private inline fun <reified T> createApi(retrofit: Retrofit) = retrofit.create(T::class.java)