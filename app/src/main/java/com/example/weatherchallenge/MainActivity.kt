package com.example.weatherchallenge

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherchallenge.ui.screen.WeatherScreen
import com.example.weatherchallenge.ui.screen.WeatherViewModel
import com.example.weatherchallenge.ui.theme.WeatherChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherChallengeTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                val locationLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { result ->
                    val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                    if (granted) {
                        viewModel.loadWeatherForCurrentLocation()
                    } else {
                        viewModel.initializeFromLastSearch()
                    }
                }

                LaunchedEffect(Unit) {
                    locationLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }

                WeatherScreen(
                    state = state,
                    onSearch = viewModel::searchByCity,
                    onRetry = {
                        if (state.lastCity.isNotBlank()) {
                            viewModel.searchByCity(state.lastCity)
                        } else {
                            viewModel.initializeFromLastSearch()
                        }
                    }
                )
            }
        }
    }
}
