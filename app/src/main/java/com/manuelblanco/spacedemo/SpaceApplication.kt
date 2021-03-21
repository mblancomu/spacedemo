package com.manuelblanco.spacedemo

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.manuelblanco.core.coreModules
import com.manuelblanco.spacedemo.viewmodel.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
@ExperimentalCoroutinesApi
class SpaceApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        //Adding modules for Koin
        startKoin {
            androidContext(this@SpaceApplication)
            androidLogger(Level.NONE)
            modules(coreModules + viewModelModule)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}