# Weather Challenge – Android Submission Draft

This is a clean Android MVVM draft for the coding challenge.

## What this project covers
- Search weather by **US city**
- Uses **OpenWeather Geocoding API** to convert city → coordinates
- Uses **OpenWeather Current Weather API** to fetch weather
- Downloads and displays the **weather icon**
- Requests **location permission** and loads weather by the user's current location if granted
- Persists the **last searched city** and auto-loads it on next app launch
- Uses **Kotlin + MVVM + Retrofit + Coroutines + Jetpack Compose**
- Includes **JUnit tests** for ViewModel / mapper behavior

## Tech stack
- Kotlin
- MVVM
- Retrofit
- Kotlin Coroutines
- Jetpack Compose
- Hilt
- DataStore
- JUnit

## Setup
1. Create a free API key at OpenWeather.
2. Open `local.properties`
3. Add:
   ```properties
   OPEN_WEATHER_API_KEY=YOUR_API_KEY_HERE
   ```
4. Build and run the app.

## Project structure
- `data/remote` → Retrofit APIs and DTOs
- `data/repository` → Repository implementation
- `domain/model` → UI-safe weather model
- `domain/usecase` → Search/load use cases
- `location` → Current location access
- `ui/screen` → Compose screen + ViewModel
- `ui/components` → Reusable UI widgets

## Notes for reviewers
- The app intentionally keeps the UI simple and readable since the challenge prioritizes function and code quality over visual polish.
- Defensive handling is included for:
  - empty search
  - unknown city
  - denied location permission
  - network/API failures
  - malformed responses
- The icon is loaded from OpenWeather using a small native image loader instead of additional image libraries.

## Suggested talking points while submitting
- I chose MVVM with a repository layer to keep API, business logic, and UI concerns separated.
- I used the Geocoding API because city-name weather lookup is deprecated in the challenge instructions.
- I persisted the last searched city using DataStore so the app restores state on launch.
- I handled permission and location gracefully so the app still works fully through city search even if location is denied.
