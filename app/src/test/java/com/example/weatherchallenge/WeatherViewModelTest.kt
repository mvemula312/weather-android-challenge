package com.example.weatherchallenge

import com.example.weatherchallenge.data.repository.WeatherRepository
import com.example.weatherchallenge.domain.model.WeatherInfo
import com.example.weatherchallenge.location.LocationClient
import com.example.weatherchallenge.location.UserLocation
import com.example.weatherchallenge.ui.screen.WeatherViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: WeatherRepository
    private lateinit var locationClient: LocationClient

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        locationClient = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchByCity updates weather on success`() = runTest {
        val info = WeatherInfo(
            cityName = "Chicago",
            condition = "Clouds",
            description = "Broken clouds",
            temperatureF = 72,
            feelsLikeF = 73,
            humidity = 55,
            windSpeedMph = 12,
            iconUrl = "icon"
        )
        coEvery { repository.getWeatherForCity("Chicago") } returns Result.success(info)

        val viewModel = WeatherViewModel(repository, locationClient)
        viewModel.searchByCity("Chicago")
        advanceUntilIdle()

        assertEquals("Chicago", viewModel.uiState.value.weather?.cityName)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `loadWeatherForCurrentLocation updates state when location succeeds`() = runTest {
        val info = WeatherInfo(
            cityName = "Dallas",
            condition = "Clear",
            description = "Clear sky",
            temperatureF = 89,
            feelsLikeF = 91,
            humidity = 40,
            windSpeedMph = 9,
            iconUrl = "icon"
        )
        coEvery { locationClient.getCurrentLocation() } returns Result.success(UserLocation(1.0, 2.0))
        coEvery { repository.getWeatherForCoordinates(1.0, 2.0) } returns Result.success(info)

        val viewModel = WeatherViewModel(repository, locationClient)
        viewModel.loadWeatherForCurrentLocation()
        advanceUntilIdle()

        assertEquals("Dallas", viewModel.uiState.value.weather?.cityName)
        assertEquals("Dallas", viewModel.uiState.value.lastCity)
    }
}
