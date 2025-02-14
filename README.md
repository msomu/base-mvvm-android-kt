![Language](https://img.shields.io/github/languages/top/msomu/base-mvvm-android-kt?color=B125EA&logo=kotlin&style=social)
[![Owner](https://img.shields.io/badge/by-msomu-brightgreen?logo=github&style=social)](https://github.com/msomu)
![License](https://img.shields.io/github/license/msomu/base-mvvm-android-kt.svg?style=social)

A robust foundation for modern Android applications, featuring a clean MVVM architecture with the latest Android development tools and best practices. Built to accelerate your development workflow while maintaining code quality and scalability.

> "Simplicity is the ultimate sophistication." - Leonardo da Vinci

## 🎯 Features

- **Modern Architecture**
  - MVVM (Model-View-ViewModel) design pattern
  - Clean and scalable project structure
  - Repository pattern for data management
  - Dependency injection with Hilt

- **UI Components**
  - 100% Jetpack Compose UI
  - Material 3 design system
  - Dark/Light theme support
  - Responsive layouts
  - Custom composable components

- **Tech Stack**
  - Kotlin Coroutines & Flow for async operations
  - Jetpack Navigation for seamless navigation
  - Retrofit & OkHttp for networking
  - Kotlinx Serialization for JSON parsing
  - Coil for image loading
  - Unit testing setup with JUnit

- **Development Tools**
  - Gradle Kotlin DSL build scripts
  - Version catalog for dependency management
  - Detekt for static code analysis
  - GitHub Actions CI/CD pipeline
  - Dependabot integration

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK with minimum API 24

### Setup
1. Click the "Use this template" button on GitHub
2. Create a new repository from the template
3. Clone the repository
```bash
git clone https://github.com/msomu/base-mvvm-android-kt.git
```

4. Open the project in Android Studio

5. Update the package name:
  - Modify `namespace` in app/build.gradle.kts
  - Update package structure in src/main/java/
  - Change applicationId in app/build.gradle.kts

6. Configure your API endpoint:
  - Create a `local.properties` file in the root directory
  - Add your base URL: `BASE_URL=https://your-api-endpoint.com/`

7. Build and run the project

## 🏗️ Project Structure

```
app/src/main/
├── java/com/msomu/androidkt/
│   ├── di/           # Dependency injection modules
│   ├── model/        # Data models
│   ├── network/      # API services and network utilities
│   ├── presentation/ # UI components and ViewModels
│   ├── repository/   # Data repositories
│   └── utils/        # Utility classes
└── res/              # Resources
```

## 🧪 Testing

The project includes both unit tests and instrumented tests:

```bash
# Run unit tests
./gradlew test

# Run instrumentation tests
./gradlew connectedAndroidTest

# Run detekt analysis
./gradlew detekt
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.

## 📫 Contact

Somasundaram Mahesh - [@msomu](https://github.com/msomu)

Project Link: [https://github.com/msomu/base-mvvm-android-kt](https://github.com/msomu/base-mvvm-android-kt)

## 🙏 Acknowledgments

- [Material Design 3](https://m3.material.io/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Android Architecture Components](https://developer.android.com/topic/architecture)
- [kotlin-android-mvvm-template](https://github.com/its-me-debk007/kotlin-android-mvvm-template)