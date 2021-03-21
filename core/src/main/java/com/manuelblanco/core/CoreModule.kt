package com.manuelblanco.core

import com.manuelblanco.core.remote.SpaceApi
import com.manuelblanco.core.remote.remoteModule
import com.manuelblanco.core.repository.SpaceRepository
import org.koin.dsl.module

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
internal val coreModule = module {
    fun provideSpaceRepository(api: SpaceApi): SpaceRepository {
        return SpaceRepository(api)
    }

    single { provideSpaceRepository(get()) }
}

val coreModules = listOf(coreModule, remoteModule)