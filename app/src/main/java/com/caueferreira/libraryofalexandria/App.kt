package com.caueferreira.libraryofalexandria

import android.app.Application
import com.caueferreira.network.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class App : Application() {

    private val appModules by lazy {
        listOf(networkModule)
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModules)
        }
    }
}