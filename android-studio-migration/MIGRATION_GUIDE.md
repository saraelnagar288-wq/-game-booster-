# Migration Guide
This guide details how to transition the GameBoost AI React/TypeScript web prototype into a native Android application.

## Architecture
- **UI:** Jetpack Compose (Material 3)
- **State Management:** ViewModel + StateFlow
- **Async Operations:** Kotlin Coroutines
- **Persistence:** Room (History, Saved Devices) & DataStore (Settings)
- **Dependency Injection:** Hilt (Recommended)
