package com.example.weatherchallenge.data.model

data class WeatherResponse(
    val name: String?,
    val weather: List<WeatherDescription>?,
    val main: MainInfo?,
    val wind: WindInfo?
)

data class WeatherDescription(
    val main: String?,
    val description: String?,
    val icon: String?
)

data class MainInfo(
    val temp: Double?,
    val feels_like: Double?,
    val humidity: Int?
)

data class WindInfo(
    val speed: Double?
)
