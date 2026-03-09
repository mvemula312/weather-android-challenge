package com.example.weatherchallenge.location

data class UserLocation(
    val latitude: Double,
    val longitude: Double
)

interface LocationClient {
    suspend fun getCurrentLocation(): Result<UserLocation>
}
