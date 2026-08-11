# GameBoost AI (Native Android Version)

This is the fully native Android implementation of **GameBoost AI**, architected according to the "Honesty System".

## Requirements

- **Android Studio** (Jellyfish or later recommended)
- **JDK 17+**
- **Android SDK 36**

## Architecture

This project was built using a modern Android stack:
- **Kotlin**
- **Jetpack Compose** (Material 3)
- **ViewModel & StateFlow**
- **Retrofit & Coroutines**

### Honesty System in Native Android

Unlike the browser-based PWA prototype which uses `navigator` and `webgl` APIs, this native Android version uses actual Android System Services:
- **`ActivityManager`**: Detects real physical RAM, available RAM, and memory pressure.
- **`BatteryManager`**: Retrieves exact battery levels, status, and plug types.
- **`PowerManager`**: Analyzes actual Thermal Status APIs (`THERMAL_STATUS_NORMAL`, `THERMAL_STATUS_CRITICAL`, etc.) provided by Android 10+.
- **`WindowManager`**: Extracts real display resolutions and supported refresh rates.
- **CPU & GPU**: Detects core counts and CPU architectures. On Samsung devices (like the target Galaxy A07), hardware profiles fall back safely without fabricating frequency values Android restricts from user-space apps.

### Running the App

1. Open Android Studio.
2. Select **File -> Open** and navigate to the `android/` directory in this project.
3. Allow Gradle to sync.
4. Select your emulator or physical device (Android 13+).
5. Click **Run**.
