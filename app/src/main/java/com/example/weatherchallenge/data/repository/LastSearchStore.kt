package com.example.weatherchallenge.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class LastSearchStore(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val LAST_CITY = stringPreferencesKey("last_city")
    }

    suspend fun saveLastCity(city: String) {
        dataStore.edit { prefs ->
            prefs[LAST_CITY] = city
        }
    }

    suspend fun getLastCity(): String? {
        return dataStore.data.first()[LAST_CITY]
    }
}
