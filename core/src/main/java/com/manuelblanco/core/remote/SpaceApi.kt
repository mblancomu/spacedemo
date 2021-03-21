package com.manuelblanco.core.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
interface SpaceApi {

    @GET("test/products_list.php")
    suspend fun getProducts(
        @Header("Authorization") authHeader: String,
        @Query("latitude") lat: String,
        @Query("longitude") lng: String,
        @Query("page") page: Int
    ): SpaceResponse
}