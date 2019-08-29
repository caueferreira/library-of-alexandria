package com.libraryofalexandria.core.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    abstract fun handleAction(action: Action)
}