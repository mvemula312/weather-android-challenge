package com.example.weatherchallenge.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.weatherchallenge.domain.model.WeatherInfo

@Composable
fun WeatherStats(weather: WeatherInfo) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Condition: ${weather.condition}")
        Text("Temperature: ${weather.temperatureF}°F")
        Text("Feels like: ${weather.feelsLikeF}°F")
        Text("Humidity: ${weather.humidity}%")
        Text("Wind: ${weather.windSpeedMph} mph")
    }
}
