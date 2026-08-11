# Test Plan

- **Dashboard:** Loads Gaming Score, UI layout validation.
- **Navigation:** Bottom navigation state integrity.
- **Game detection & manual addition:** Ensures `QUERY_ALL_PACKAGES` falls back safely if denied.
- **Game launching:** Validates intents to launch game packages.
- **Device analyzer:**
  - GPU detection via EGL
  - RAM via ActivityManager
  - Battery via BatteryManager
  - Thermal via PowerManager
  - Display via WindowManager
- **Gaming Score calculation:** Validation against base hardware tiers.
- **FPS estimation logic:** Ensures boundaries and confidence intervals scale properly.
- **AI Assistant:** Validate Gemini API integrations and safe contextual prompts.
- **History & Settings:** Room and DataStore persistence checks.
- **Dark mode:** Compose material theme validation.
- **Android 16:** Edge-to-edge layout checks, predictive back gesture.
- **Samsung Galaxy A07:** Ensure hardware detection fallback mechanism functions correctly.
