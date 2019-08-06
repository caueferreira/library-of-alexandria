package com.libraryofalexandria.cards.di

import com.libraryofalexandria.cache.Cache
import com.libraryofalexandria.cards.data.ExpansionRepository
import com.libraryofalexandria.cards.data.FiltersRepository
import com.libraryofalexandria.cards.data.local.FiltersLocalDataSource
import com.libraryofalexandria.cards.data.local.ExpansionsLocalDataSource
import com.libraryofalexandria.cards.data.network.CardsRemoteDataSource
import com.libraryofalexandria.cards.data.network.ScryfallApi
import com.libraryofalexandria.cards.data.network.ExpansionsRemoteDataSource
import com.libraryofalexandria.cards.domain.Expansion
import com.libraryofalexandria.cards.domain.FetchCards
import com.libraryofalexandria.cards.domain.FetchExpansions
import com.libraryofalexandria.cards.view.cards.CardsViewModel
import com.libraryofalexandria.cards.view.expansions.ExpansionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single { createApi<ScryfallApi>(get()) }
}

val cardsModule = module {
    single { provideCardsRemoteDataSource(get()) }
    single { provideCardsUseCase(get()) }
    viewModel { CardsViewModel(get()) }
}

val filtersModule = module {
    single { FiltersLocalDataSource() }
    single { FiltersRepository(get()) }
}

val expansionsModule = module {
    single { ExpansionsRemoteDataSource(get()) }
    single { Cache(get(), "expansions-cache", Expansion::class.java) }
    single { ExpansionsLocalDataSource(get()) }
    single { ExpansionRepository(get(), get()) }
    single { FetchExpansions(get()) }
    viewModel { ExpansionViewModel(get(), get()) }
}


private fun provideCardsRemoteDataSource(scryfallApi: ScryfallApi): CardsRemoteDataSource =
    CardsRemoteDataSource(scryfallApi)

private fun provideCardsUseCase(remoteDataSource: CardsRemoteDataSource) =
    FetchCards(remoteDataSource)


private inline fun <reified T> createApi(retrofit: Retrofit) = retrofit.create(T::class.java)