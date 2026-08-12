# WeatherTrio — COMP3025 Assignment 3

## Project Overview

WeatherTrio is an Android weather application developed for COMP3025 — Mobile and Pervasive Computing.

This final release builds on the functionality from Assignments 1 and 2 and includes dynamic city search, live weather data, Firebase Authentication, Cloud Firestore saved locations, persisted settings, light/dark themes, and Java/Kotlin interoperability.

## Main Features

- Email/password registration and login using Firebase Authentication
- Persistent Firebase user sessions
- Dynamic city search using the Open-Meteo Geocoding API
- 300 ms debounced search input
- RecyclerView search results
- Live weather details using Open-Meteo
- Cloud Firestore saved locations tied to the authenticated user's UID
- Save and remove location functionality
- Real-time saved-location updates
- Celsius and Fahrenheit preference
- Light and dark theme preference
- SharedPreferences for settings persistence
- Kotlin utility/settings files alongside the Java application code
- Friendly empty, loading, and network-error states
- Retry functionality after a failed weather request
- Existing Assignment 1 and Assignment 2 functionality retained

## Architecture

The application follows an MVVM-style structure with separate packages for:

- `model`
- `repository`
- `view`
- `viewmodel`
- `preferences`
- `util`

Networking is handled in repositories using OkHttp and manual JSON parsing.

Firebase Authentication manages user accounts and sessions, while Cloud Firestore stores saved locations associated with each authenticated user.

## Technologies Used

- Java
- Kotlin
- Android SDK
- ViewBinding
- LiveData / ViewModel
- RecyclerView
- OkHttp
- Open-Meteo APIs
- Firebase Authentication
- Cloud Firestore
- SharedPreferences
- AppCompatDelegate

## Video Demonstration

The full Assignment 3 application demonstration and code walkthrough is available on YouTube:

[Watch the WeatherTrio Assignment 3 Video](https://youtu.be/bPWem-8o_6U)

## Build Notes

### Requirements

- Android Studio
- Android SDK with API 34 installed
- Compatible JDK
- Internet connection for Gradle dependencies, Firebase, and weather data

The Firebase configuration file (`app/google-services.json`) is included with the project.

### Build Instructions

1. Extract the project ZIP.
2. Open the `WeatherTrio-Assignment3-main` folder in Android Studio.
3. Allow Android Studio to complete the Gradle sync.
4. Select or create an Android emulator compatible with the project's minimum SDK.
5. Run the application from Android Studio.

Alternatively, from the project root on Windows, a debug build can be created with:

```powershell
(.\gradlew.bat assembleDebug)
```

A configured Android SDK and compatible JDK are required to build and run the application.

local.properties is intentionally not included because Android Studio generates it using the Android SDK location on the current computer.
