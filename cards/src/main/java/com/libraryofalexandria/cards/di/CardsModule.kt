package com.libraryofalexandria.cards.di

import com.libraryofalexandria.cards.data.network.CardNetworkRepository
import com.libraryofalexandria.cards.data.network.ScryfallApi
import com.libraryofalexandria.cards.data.network.SetNetworkRepository
import com.libraryofalexandria.cards.view.cards.CardsViewModel
import com.libraryofalexandria.cards.view.sets.SetsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val cardsModule = module {
    single { createApi<ScryfallApi>(get()) }

    single { provideCardsRepository(get()) }
    viewModel { CardsViewModel(get()) }

    single { provideSetsRepository(get()) }
    viewModel { SetsViewModel(get()) }
}

private fun provideCardsRepository(scryfallApi: ScryfallApi): CardNetworkRepository =
    CardNetworkRepository(scryfallApi)

private fun provideSetsRepository(scryfallApi: ScryfallApi): SetNetworkRepository =
    SetNetworkRepository(scryfallApi)

private inline fun <reified T> createApi(retrofit: Retrofit) = retrofit.create(T::class.java)