package com.example.weatherchallenge.data.repository

import com.example.weatherchallenge.data.model.WeatherResponse
import com.example.weatherchallenge.data.remote.GeocodingApi
import com.example.weatherchallenge.data.remote.WeatherApi
import com.example.weatherchallenge.domain.model.WeatherInfo
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val geocodingApi: GeocodingApi,
    private val weatherApi: WeatherApi,
    private val lastSearchStore: LastSearchStore,
    private val apiKey: String
) : WeatherRepository {

    override suspend fun getWeatherForCity(city: String): Result<WeatherInfo> {
        return runCatching {
            require(city.isNotBlank()) { "Please enter a city name." }
            validateApiKey()
            val geo = geocodingApi.getCoordinatesForCity(
                city = city.trim(),
                appId = apiKey
            ).firstOrNull() ?: throw IllegalArgumentException("City not found.")

            val lat = geo.lat ?: throw IOException("Missing latitude from geocoding response.")
            val lon = geo.lon ?: throw IOException("Missing longitude from geocoding response.")

            val weather = weatherApi.getWeatherByCoordinates(lat = lat, lon = lon, appId = apiKey)
            val result = weather.toDomain()
            saveLastCity(result.cityName)
            result
        }.mapFailure()
    }

    override suspend fun getWeatherForCoordinates(lat: Double, lon: Double): Result<WeatherInfo> {
        return runCatching {
            validateApiKey()
            val weather = weatherApi.getWeatherByCoordinates(lat = lat, lon = lon, appId = apiKey)
            weather.toDomain()
        }.mapFailure()
    }

    override suspend fun saveLastCity(city: String) {
        lastSearchStore.saveLastCity(city)
    }

    override suspend fun getLastCity(): String? = lastSearchStore.getLastCity()

    private fun validateApiKey() {
        require(apiKey.isNotBlank()) {
            "Missing OpenWeather API key. Add OPEN_WEATHER_API_KEY in local.properties."
        }
    }
}

private fun WeatherResponse.toDomain(): WeatherInfo {
    val item = weather?.firstOrNull()
        ?: throw IOException("Weather details are unavailable right now.")

    return WeatherInfo(
        cityName = name.orEmpty().ifBlank { "Unknown City" },
        condition = item.main.orEmpty().ifBlank { "Unknown" },
        description = item.description.orEmpty().replaceFirstChar { it.uppercase() },
        temperatureF = main?.temp?.toInt() ?: 0,
        feelsLikeF = main?.feels_like?.toInt() ?: 0,
        humidity = main?.humidity ?: 0,
        windSpeedMph = wind?.speed?.toInt() ?: 0,
        iconUrl = "https://openweathermap.org/img/wn/${item.icon ?: "01d"}@2x.png"
    )
}

private fun <T> Result<T>.mapFailure(): Result<T> {
    return fold(
        onSuccess = { Result.success(it) },
        onFailure = { throwable ->
            val message = when (throwable) {
                is IllegalArgumentException -> throwable.message ?: "Invalid input."
                is IOException -> throwable.message ?: "Network or data error."
                else -> "Something went wrong. Please try again."
            }
            Result.failure(RuntimeException(message, throwable))
        }
    )
}
