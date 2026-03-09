package com.example.weatherchallenge.domain.model

data class WeatherInfo(
    val cityName: String,
    val condition: String,
    val description: String,
    val temperatureF: Int,
    val feelsLikeF: Int,
    val humidity: Int,
    val windSpeedMph: Int,
    val iconUrl: String
)
