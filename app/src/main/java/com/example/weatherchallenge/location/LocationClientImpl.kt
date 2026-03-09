package com.example.weatherchallenge.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationClientImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationClient {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Result<UserLocation> {
        return suspendCancellableCoroutine { continuation ->
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location == null) {
                        continuation.resume(Result.failure(RuntimeException("Location unavailable.")))
                    } else {
                        continuation.resume(
                            Result.success(
                                UserLocation(
                                    latitude = location.latitude,
                                    longitude = location.longitude
                                )
                            )
                        )
                    }
                }
                .addOnFailureListener {
                    continuation.resume(
                        Result.failure(RuntimeException("Unable to get current location."))
                    )
                }
        }
    }
}
