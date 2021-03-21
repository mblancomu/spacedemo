package com.manuelblanco.spacedemo.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module
/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */

/**
 * Module for Koin, in this case for ViewModels.
 */
@ExperimentalCoroutinesApi
internal val viewModelModule = module {
    single { MainViewModel(get()) }
    single { ListViewModel() }
    single { MapViewModel() }
    single { DetailViewModel() }
}