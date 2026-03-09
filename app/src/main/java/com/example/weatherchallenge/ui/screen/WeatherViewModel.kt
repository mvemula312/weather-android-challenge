package com.example.weatherchallenge.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherchallenge.data.repository.WeatherRepository
import com.example.weatherchallenge.location.LocationClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationClient: LocationClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun initializeFromLastSearch() {
        viewModelScope.launch {
            val lastCity = repository.getLastCity().orEmpty()
            _uiState.value = _uiState.value.copy(lastCity = lastCity)
            if (lastCity.isNotBlank()) {
                searchByCity(lastCity)
            }
        }
    }

    fun searchByCity(city: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                searchText = city
            )

            repository.getWeatherForCity(city)
                .onSuccess { weather ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weather = weather,
                        errorMessage = null,
                        lastCity = weather.cityName,
                        searchText = weather.cityName
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message,
                        weather = null
                    )
                }
        }
    }

    fun loadWeatherForCurrentLocation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            locationClient.getCurrentLocation()
                .onSuccess { location ->
                    repository.getWeatherForCoordinates(
                        lat = location.latitude,
                        lon = location.longitude
                    ).onSuccess { weather ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            weather = weather,
                            errorMessage = null,
                            searchText = weather.cityName,
                            lastCity = weather.cityName
                        )
                        repository.saveLastCity(weather.cityName)
                    }.onFailure { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    initializeFromLastSearch()
                }
        }
    }
}
