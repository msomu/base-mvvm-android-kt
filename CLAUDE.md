# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a modern Android application template using MVVM architecture with 100% Jetpack Compose UI. The project demonstrates clean architecture principles with Hilt for dependency injection, Retrofit for networking, and Kotlin Coroutines/Flow for asynchronous operations.

**Tech Stack:**
- Kotlin with Coroutines & Flow
- Jetpack Compose (100% UI)
- Material 3 design system
- Dagger Hilt (dependency injection)
- Retrofit + OkHttp (networking)
- Kotlinx Serialization (JSON parsing)
- Coil (image loading)
- Detekt (static code analysis)

**Build Requirements:**
- Android Studio Hedgehog or later
- JDK 17 (though project compiles with Java 11)
- Android SDK with minimum API 24, target API 36

## Common Commands

### Building & Running
```bash
# Build the project
./gradlew build

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install and run on connected device/emulator
./gradlew installDebug
```

### Testing
```bash
# Run unit tests
./gradlew test

# Run unit tests for debug build
./gradlew testDebugUnitTest

# Run instrumented/UI tests
./gradlew connectedAndroidTest

# Run specific test class
./gradlew test --tests com.msomu.androidkt.SpecificTestClass

# Run tests with coverage report
./gradlew testDebugUnitTest jacocoTestReport
```

### Code Quality
```bash
# Run Detekt static code analysis
./gradlew detekt

# Detekt config is located at: config/detekt/detekt.yml
```

### Dependency Management
```bash
# Check for dependency updates
./gradlew dependencyUpdates

# Dependencies are managed via version catalog at: gradle/libs.versions.toml
```

### Clean & Sync
```bash
# Clean build artifacts
./gradlew clean

# Clean and rebuild
./gradlew clean build
```

## Architecture & Code Organization

### MVVM Pattern Implementation

The project follows a strict MVVM (Model-View-ViewModel) architecture with clear separation of concerns:

**Layers:**
1. **Presentation Layer** (`presentation/`): Compose UI screens, navigation, and ViewModels
2. **Domain Layer** (Repository interface): Business logic abstraction
3. **Data Layer** (`repository/`, `network/`): Data fetching and API implementations

### Dependency Injection with Hilt

Hilt modules are organized by responsibility:
- **NetworkModule** (`di/NetworkModule.kt`): Provides Retrofit instance and API service
  - Reads BASE_URL from BuildConfig (configured in `local.properties`)
  - Uses Kotlinx Serialization converter
- **RepositoryModule** (`di/RepositoryModule.kt`): Provides Repository implementation

**Key Pattern:** All modules are installed in `SingletonComponent` and provide `@Singleton` scoped instances.

### State Management

**ApiState Sealed Class** (`network/ApiState.kt`): Wraps all API responses with three states:
- `Success<T>`: Contains successful response data
- `Error`: Contains error message
- `Loading`: Indicates loading state

**UI State Pattern:** Each ViewModel defines its own sealed class for UI states (e.g., `HomeUiState` in `HomeViewModel.kt`):
```kotlin
sealed class HomeUiState {
    data class Success(val todoItems: List<TodoItem>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
    data object Loading : HomeUiState()
}
```

ViewModels expose state as `StateFlow` and UI components collect state using `collectAsState()` in Compose.

### Navigation

Navigation uses Jetpack Navigation Compose with type-safe route constants defined in `NavRoutes.kt`. Routes are simple string constants (e.g., `HOME`, `DETAIL`).

### Repository Pattern

All data operations go through the Repository interface:
- **Repository** (interface): Defines data operation contracts
- **RepositoryImpl** (implementation): Implements operations with try-catch error handling
  - Wraps successful responses in `ApiState.Success`
  - Catches exceptions and wraps in `ApiState.Error`

### API Configuration

The BASE_URL is configured via BuildConfig:
1. Add `BASE_URL=https://your-api-endpoint.com/` to `local.properties`
2. If `local.properties` doesn't exist or BASE_URL isn't set, defaults to `https://jsonplaceholder.typicode.com/`
3. BuildConfig field is generated in `app/build.gradle.kts:39`

## Key Conventions

### Package Structure
```
com.msomu.androidkt/
├── di/                    # Hilt modules
├── model/                 # Data models (use @Serializable)
├── network/               # API service and ApiState
├── presentation/          # All UI code
│   ├── ui/
│   │   ├── components/    # Reusable Compose components
│   │   ├── screen/        # Screen-level composables
│   │   └── theme/         # Material 3 theming
│   ├── viewmodel/         # HiltViewModel classes
│   ├── App.kt             # Main navigation setup
│   └── NavRoutes.kt       # Route constants
├── repository/            # Repository interface and impl
└── utils/                 # Utility classes
```

### ViewModel Pattern
- All ViewModels must be annotated with `@HiltViewModel`
- Constructor inject Repository dependencies
- Expose UI state as `StateFlow`
- Use `viewModelScope` for coroutines
- Initialize data fetching in `init` block when appropriate

### Compose UI Pattern
- Use Material 3 components
- Follow unidirectional data flow
- Hoist state to ViewModels
- Use `collectAsState()` to observe StateFlow
- Preview functions for UI components

### Serialization
- Use `@Serializable` annotation on data classes
- Kotlinx Serialization is configured for Retrofit converter

### Naming Conventions
- ViewModels: `[Feature]ViewModel.kt` (e.g., `HomeViewModel`, `DetailViewModel`)
- Screens: `[ScreenName].kt` in `presentation/ui/screen/` (e.g., `Home.kt`, `Detail.kt`)
- Components: Descriptive names in `presentation/ui/components/`
- Repository: Interface named `Repository`, implementation named `RepositoryImpl`

## Configuration Files

- **gradle/libs.versions.toml**: Version catalog for all dependencies
- **config/detekt/detekt.yml**: Detekt static analysis configuration
- **local.properties**: Local configuration (not committed, contains BASE_URL)
- **app/build.gradle.kts**: App module build configuration
  - Namespace: `com.msomu.androidkt`
  - Min SDK: 24, Target SDK: 36, Compile SDK: 36

## CI/CD

The project includes GitHub Actions workflows in `.github/workflows/`:
- Automated build verification
- Dependency updates via Dependabot
