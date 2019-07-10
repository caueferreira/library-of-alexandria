package com.libraryofalexandria.cards.view.sets

import android.view.View
import androidx.lifecycle.*
import com.libraryofalexandria.cards.data.FiltersRepository
import com.libraryofalexandria.cards.domain.FetchSets
import com.libraryofalexandria.cards.domain.Set
import com.libraryofalexandria.cards.domain.SetsResult
import com.libraryofalexandria.cards.view.sets.transformers.SetViewEntityMapper
import com.libraryofalexandria.cards.view.sets.ui.FilterViewEntity
import com.libraryofalexandria.cards.view.sets.ui.SetViewEntity
import com.libraryofalexandria.cards.view.sets.ui.SetsViewState
import com.libraryofalexandria.core.extensions.addOrRemove
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.stream.Collectors.toList

class SetsViewModel(
    private val fetchSets: FetchSets,
    private val filterRepository: FiltersRepository,
    private val viewState: SetsViewState = SetsViewState(),
    private val mapper: SetViewEntityMapper = SetViewEntityMapper()
) : ViewModel() {

    val state = liveData {
        fetchSets.fetch()
            .collect {
                when (it) {
                    is SetsResult.Loading -> emit(viewState.copy(isLoading = View.VISIBLE))
                    is SetsResult.Success.Cache -> emit(showSets(it.result))
                    is SetsResult.Success.Network -> emit(showSets(it.result))
                    is SetsResult.Failure -> emit(viewState.copy(throwable = it.error))
                }
            }
    }

    val filters = liveData {
        filterRepository.get().collect {
            emit(it)
        }
    }

    private fun showSets(result: List<Set>) =
        viewState.copy(isLoading = View.INVISIBLE, sets = result.stream()
            .map { mapper.transform(it) }
            .collect(toList())
        )

}