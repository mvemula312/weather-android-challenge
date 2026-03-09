package com.example.weatherchallenge.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun WeatherIcon(
    iconUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val imageBitmap by produceState<androidx.compose.ui.graphics.ImageBitmap?>(initialValue = null, iconUrl) {
        value = loadBitmap(iconUrl)
    }

    imageBitmap?.let {
        Image(
            bitmap = it,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Fit
        )
    }
}

private suspend fun loadBitmap(url: String): androidx.compose.ui.graphics.ImageBitmap? {
    return withContext(Dispatchers.IO) {
        runCatching {
            URL(url).openStream().use { input ->
                BitmapFactory.decodeStream(input)?.asImageBitmap()
            }
        }.getOrNull()
    }
}
