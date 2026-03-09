# Submission Draft

Hello Team,

Please find my Android weather challenge submission attached.

### Approach
I implemented the solution using **Kotlin + MVVM**, with a clean separation between UI, ViewModel, repository, remote API layer, and local persistence.

### What is included
- Search weather by US city
- Geocoding API usage for city-to-coordinate lookup
- Current weather retrieval using coordinates
- Weather icon download and display
- Location permission handling and default load from current location when available
- Auto-load of the last searched city on app launch
- Defensive error handling for blank input, city not found, location denial, and API/network failures
- Unit tests for ViewModel behavior

### Architecture decisions
- **MVVM** for clarity, testability, and separation of concerns
- **Repository pattern** to isolate networking and persistence
- **DataStore** to persist the last searched city
- **Retrofit** for network communication
- **Compose** for a concise and maintainable UI layer
- **Hilt** for dependency injection

### Notes
- I used the Geocoding API as recommended in the challenge because city-name weather endpoints are deprecated.
- The UI is intentionally simple, with focus on correctness, maintainability, and readable code.

Thank you for your time and review.
