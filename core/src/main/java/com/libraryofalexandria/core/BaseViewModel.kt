package com.libraryofalexandria.core

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    abstract fun handleAction(action: Action)
}