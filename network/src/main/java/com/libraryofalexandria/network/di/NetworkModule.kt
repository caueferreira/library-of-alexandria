package com.libraryofalexandria.network.di

import android.content.Context
import com.libraryofalexandria.network.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideCache(get()) }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
}

private const val CONNECTION_TIMEOUT = 6000L
private const val CACHE_SIZE = (10 * 1024 * 1024).toLong()

private fun provideOkHttpClient(cache: Cache): OkHttpClient {
    val client = OkHttpClient.Builder()
        .cache(cache)
        .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)

    if (BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        client.addInterceptor(logging)
    }

    return client.build()
}

private fun provideCache(context: Context) = Cache(context.applicationContext.cacheDir,
    CACHE_SIZE
)

private fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
    .baseUrl(BuildConfig.SCRYFALL_API_ENDPOINT)
    .addConverterFactory(MoshiConverterFactory.create(createMoshi()))
    .client(okHttpClient)
    .build()

private fun createMoshi() = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()