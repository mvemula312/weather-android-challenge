package com.example.weatherchallenge.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherchallenge.BuildConfig
import com.example.weatherchallenge.data.remote.GeocodingApi
import com.example.weatherchallenge.data.remote.WeatherApi
import com.example.weatherchallenge.data.repository.LastSearchStore
import com.example.weatherchallenge.data.repository.WeatherRepository
import com.example.weatherchallenge.data.repository.WeatherRepositoryImpl
import com.example.weatherchallenge.location.LocationClient
import com.example.weatherchallenge.location.LocationClientImpl
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "weather_prefs")

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideGeocodingApi(retrofit: Retrofit): GeocodingApi =
        retrofit.create(GeocodingApi::class.java)

    @Provides
    @Singleton
    fun provideApiKey(): String = BuildConfig.OPEN_WEATHER_API_KEY

    @Provides
    @Singleton
    fun provideLastSearchStore(@ApplicationContext context: Context): LastSearchStore {
        return LastSearchStore(context.dataStore)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindLocationClient(
        impl: LocationClientImpl
    ): LocationClient
}
