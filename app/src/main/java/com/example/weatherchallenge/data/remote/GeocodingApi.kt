package com.example.weatherchallenge.data.remote

import com.example.weatherchallenge.data.model.GeoCodeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {
    @GET("geo/1.0/direct")
    suspend fun getCoordinatesForCity(
        @Query("q") city: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") appId: String
    ): List<GeoCodeResponse>
}
