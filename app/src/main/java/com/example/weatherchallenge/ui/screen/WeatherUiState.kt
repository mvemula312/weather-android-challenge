package com.example.weatherchallenge.ui.screen

import com.example.weatherchallenge.domain.model.WeatherInfo

data class WeatherUiState(
    val isLoading: Boolean = false,
    val searchText: String = "",
    val weather: WeatherInfo? = null,
    val errorMessage: String? = null,
    val lastCity: String = ""
)
