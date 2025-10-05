## BD Price Monitor (Android, Kotlin, Compose)

Minimal Android 14+ app that shows official prices from Bangladesh government sources with offline cache, dark theme, and background sync.

### Build

- Requirements: Android Studio Jellyfish/Koala, JDK 17, Android SDK 34
- Clone and open in Android Studio
- Run on device/emulator (Android 8.0+)

### Tech
- Jetpack Compose (Material 3, dark theme)
- Navigation Compose
- Room (local cache)
- Retrofit + Kotlinx Serialization (API client)
- WorkManager (background daily sync)
- DataStore (settings)
- MVVM (ViewModel + Repository)

### Run flow
- On first launch, app seeds DB from `app/src/main/assets/seed.json`
- Home shows categories → subcategories → products
- Product detail shows price, unit, gov body, last updated, and source link
- Search queries local DB
- Background sync runs every 24h, configurable in Settings

### Configure backend
- BASE_URL is set in `app/build.gradle.kts` BuildConfig; replace `https://example.com/` with your API
- Expected endpoint: `/api/snapshot` returning JSON of categories, subcategories, products

### Module layout
- `data/local`: Room entities, DAOs, database
- `data/remote`: Retrofit service and DTOs
- `data/Repository.kt`: Sync + seed logic
- `ui/` screens and theme, `navigation/` NavHost
- `sync/SyncWorker.kt`: WorkManager
- `settings/SettingsStore.kt`: DataStore

### Notes
- Icons use Material Icons Extended
- This is an MVP scaffold; refine UI, add tests, and integrate real backend scrapers (FastAPI) next.
