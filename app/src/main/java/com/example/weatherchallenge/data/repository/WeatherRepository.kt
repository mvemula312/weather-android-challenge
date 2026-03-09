package com.example.weatherchallenge.data.repository

import com.example.weatherchallenge.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherForCity(city: String): Result<WeatherInfo>
    suspend fun getWeatherForCoordinates(lat: Double, lon: Double): Result<WeatherInfo>
    suspend fun saveLastCity(city: String)
    suspend fun getLastCity(): String?
}
