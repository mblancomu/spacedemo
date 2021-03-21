package com.manuelblanco.core.repository

import android.util.Base64
import com.manuelblanco.core.BuildConfig
import com.manuelblanco.core.model.Resource
import com.manuelblanco.core.remote.SpaceApi
import com.manuelblanco.core.remote.SpaceResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.UnsupportedEncodingException

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
class SpaceRepository(private val api: SpaceApi) : ISpaceRepository {

    override fun getProducts(lat: String, lng: String, page: Int): Flow<Resource<SpaceResponse>> =
        flow {
            emit(Resource.Loading)
            val resource = try {
                val response = api.getProducts(getAuthToken(), lat, lng, page)
                Resource.Success(response)
            } catch (e: Throwable) {
                Resource.Fail(e)
            }
            emit(resource)
        }

    private fun getAuthToken(): String {
        var data = ByteArray(0)
        try {
            data =
                (BuildConfig.USER_SPACE.plus(":").plus(BuildConfig.PASSWORD_SPACE)).toByteArray(
                    charset("UTF-8")
                )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP)
    }
}