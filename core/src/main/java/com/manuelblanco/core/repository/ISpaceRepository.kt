package com.manuelblanco.core.repository

import com.manuelblanco.core.model.Resource
import com.manuelblanco.core.remote.SpaceResponse
import kotlinx.coroutines.flow.Flow

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
interface ISpaceRepository {
    fun getProducts(lat: String, lng: String, page: Int): Flow<Resource<SpaceResponse>>
}