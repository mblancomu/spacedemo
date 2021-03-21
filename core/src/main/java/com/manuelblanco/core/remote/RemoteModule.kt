package com.manuelblanco.core.remote

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */

internal val remoteModule = module {
    single { provideApi<SpaceApi>(okHttpClient = get(), gson = get()) }
    factory { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY } }
    single { provideOkHttpClient(loggingInterceptor = get()) }
    single { provideGson() }
    single { provideCache(androidApplication()) }
}

private fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient = OkHttpClient().newBuilder().apply {
    if (BuildConfig.BUILD_TYPE.equals("debug")) addInterceptor(loggingInterceptor)
}.build()

private inline fun <reified T> provideApi(
    okHttpClient: OkHttpClient,
    gson: Gson
): T = Retrofit.Builder().apply {
    baseUrl("https://devapis.spacelens.com/")
    client(okHttpClient)
    addConverterFactory(GsonConverterFactory.create(gson))
}.build().create(T::class.java)

private fun provideGson(): Gson {
    return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
}

private fun provideCache(application: Application): Cache {
    val cacheSize = 10 * 1024 * 1024
    return Cache(application.cacheDir, cacheSize.toLong())
}