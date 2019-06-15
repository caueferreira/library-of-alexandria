package com.libraryofalexandria.cards.view

import androidx.lifecycle.MutableLiveData

class ViewState(private val state: MutableLiveData<State> = MutableLiveData()) {
    private var _state: MutableLiveData<State> = MutableLiveData()

    fun update(state: State) {
        this._state.postValue(state)
    }

    fun state() = _state
}