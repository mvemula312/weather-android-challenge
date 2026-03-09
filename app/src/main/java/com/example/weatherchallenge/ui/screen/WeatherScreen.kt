package com.example.weatherchallenge.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherchallenge.ui.components.WeatherIcon
import com.example.weatherchallenge.ui.components.WeatherStats

@Composable
fun WeatherScreen(
    state: WeatherUiState,
    onSearch: (String) -> Unit,
    onRetry: () -> Unit
) {
    var city by remember(state.searchText) { mutableStateOf(state.searchText) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Weather Lookup",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Enter a US city") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = { onSearch(city) },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Text("Search")
            }

            if (state.isLoading) {
                CircularProgressIndicator()
            }

            state.errorMessage?.let { error ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(
                            onClick = onRetry,
                            modifier = Modifier.padding(top = 12.dp)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            state.weather?.let { weather ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = weather.cityName,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = weather.description,
                            style = MaterialTheme.typography.titleMedium
                        )
                        WeatherIcon(
                            iconUrl = weather.iconUrl,
                            contentDescription = weather.description
                        )
                        WeatherStats(weather = weather)
                    }
                }
            }
        }
    }
}
