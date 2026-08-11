# Build Instructions

1. Download/export the project from the Google AI Studio environment.
2. Open **Android Studio**.
3. Create/import the native Android project using the files located in `android-studio-migration/android-native`.
4. Copy the migration data from `android-studio-migration/migration-data`.
5. Configure `build.gradle.kts` to target SDK 36.
6. Configure Android SDK 36 within Android Studio's SDK Manager.
7. Sync Gradle.
8. Resolve dependencies (Compose, Room, Retrofit, Kotlinx).
9. Build Debug APK.
10. Install APK on an Android phone via ADB or physical transfer.
11. Test Android 16 compatibility.
12. Test Samsung Galaxy A07 hardware detection.
