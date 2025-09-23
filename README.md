# SDKForge Template

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20--Beta2-blue.svg)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-API%2023+-green.svg)](https://developer.android.com/)
[![iOS](https://img.shields.io/badge/iOS-12.0+-orange.svg)](https://developer.apple.com/ios/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-lightgrey.svg)](https://kotlinlang.org/docs/multiplatform.html)

A modern Kotlin Multiplatform SDK template for building cross-platform libraries and applications. This template provides a robust foundation for creating SDKs that work seamlessly across Android and iOS platforms. **Currently in development** - this template is designed to be forked and customized for your specific SDK needs.

## 🚀 Features

- **Kotlin Multiplatform**: Write once, run on Android and iOS
- **Modern Architecture**: Clean, modular structure with separate modules for different concerns
- **Compose Multiplatform**: Shared UI components using Jetpack Compose
- **Comprehensive Testing**: Unit tests, integration tests, and performance benchmarks
- **Code Quality**: KtLint, dependency guard, and binary compatibility validation
- **Documentation**: Automated API documentation with Dokka
- **CI/CD Ready**: GitHub Actions workflows for automated builds and publishing
- **Performance Monitoring**: Built-in benchmarking tools

## 📱 Supported Platforms

- **Android**: API 23+ (Android 6.0+)
- **iOS**: 12.0+
- **Kotlin**: 2.2.20-Beta2+
- **Gradle**: 8.0+

## 🏗️ Project Structure

```
SDKForge.Template/
├── app-android/         # Android sample application
├── app-ios/             # iOS sample application
├── app-shared/          # Shared sample UI components (Compose Multiplatform)
├── shared/              # Main SDK library with all components
├── shared-core/         # Core shared functionality
├── shared-template/     # Template for shared modules
├── build-logic/         # Gradle build logic and conventions
├── internal-benchmark/  # Performance benchmarks
└── internal-ktlint/     # Custom ktlint rules
```

## 🛠️ Installation

> **Note**: This project is currently in development and not yet published to any repository. Installation instructions will be available once the project is published.

### Future Installation (Coming Soon)

Once published, you'll be able to install the SDK using:

#### Gradle (Kotlin DSL)
```kotlin
dependencies {
    implementation("dev.sdkforge.template:shared:1.0.0")
    implementation("dev.sdkforge.template:shared-core:1.0.0")
}
```

#### Gradle (Groovy)
```groovy
dependencies {
    implementation 'dev.sdkforge.template:shared:1.0.0'
    implementation 'dev.sdkforge.template:shared-core:1.0.0'
}
```

#### Maven
```xml
<dependency>
    <groupId>dev.sdkforge.template</groupId>
    <artifactId>shared</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 🚀 Quick Start

### Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher
- **Android Studio**: Latest stable version (for Android development)
- **Xcode**: Latest stable version (for iOS development, macOS only)
- **Kotlin**: Version 2.2.20-Beta2 or higher
- **Gradle**: Version 8.0 or higher

### Development Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/SDKForge/template-sdk.git SDKForgeTemplate
   cd SDKForgeTemplate
   ```

2. **Build the project**:
   ```bash
   ./gradlew build
   ```

3. **Run tests**:
   ```bash
   ./gradlew lint ktlintCheck dependencyGuard apiCheck
   ```

4. **Generate documentation**:
   ```bash
   ./gradlew dokkaHtml
   ```

### Using as a Template

This project serves as a template for creating Kotlin Multiplatform SDKs. To use it:

1. **Fork or clone** this repository
2. **Customize** the module names, package names, and functionality
3. **Add your SDK logic** to the `shared-*` modules
4. **Configure publishing** when ready to distribute
5. **Update documentation** to reflect your specific SDK

## 📖 Usage

### Basic SDK Structure

The template provides a modular structure with platform-specific implementations:

```kotlin
// Common interface (shared/src/commonMain/kotlin/dev/sdkforge/template/core/Platform.kt)
interface Platform {
    val name: String
    val version: String
}

expect val currentPlatform: Platform
```

### Platform-Specific Implementations

#### Android Implementation
```kotlin
// shared-core/src/androidMain/kotlin/dev/sdkforge/template/core/Platform.android.kt
actual val currentPlatform: Platform = object : Platform {
    override val name: String get() = "Android"
    override val version: String get() = android.os.Build.VERSION.SDK_INT.toString()
}
```

#### iOS Implementation
```kotlin
// shared-core/src/iosMain/kotlin/dev/sdkforge/template/core/Platform.ios.kt
import platform.UIKit.UIDevice

actual val currentPlatform: Platform = object : Platform {
    override val name: String get() = UIDevice.currentDevice.systemName
    override val version: String get() = UIDevice.currentDevice.systemVersion
}
```

### Compose Multiplatform UI

The template includes a shared UI component using Compose Multiplatform:

```kotlin
// app-shared/src/commonMain/kotlin/dev/sdkforge/template/app/App.kt
@Composable
fun App(
    modifier: Modifier = Modifier,
) = ApplicationTheme {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Platform name: ${currentPlatform.name}",
            )
            Text(
                text = "Platform version: ${currentPlatform.version}",
            )
        }
    }
}
```

### SDK Version Information

Access SDK version information through the Library object:

```kotlin
// shared/src/commonMain/kotlin/dev/sdkforge/template/Library.kt
data object Library {
    const val VERSION = "0.0.1"
}

// Usage
val sdkVersion = Library.VERSION
```

## 🧪 Testing

### Run All Tests
```bash
./gradlew test
```

### Run Platform-Specific Tests
```bash
# Android tests
./gradlew app-android:test

# iOS tests
./gradlew app-ios:test
```

### Performance Benchmarks
```bash
./gradlew internal-benchmark:connectedAndroidTest
```

## 📚 Documentation

- **API Documentation**: Generated with Dokka
- **Contributing Guide**: [CONTRIBUTING.md](CONTRIBUTING.md)
- **Code of Conduct**: [CODE_OF_CONDUCT.md](.github/CODE_OF_CONDUCT.md)
- **Security Policy**: [SECURITY.md](.github/SECURITY.md)

## 🔧 Development

### Adding New Modules

1. Create a new module in the `shared-*` directory
2. Apply the appropriate plugins in `build.gradle.kts`
3. Update the main `shared` module to export the new module
4. Add tests and documentation

### Code Style

This project uses KtLint for code formatting. Run the following to check and fix code style:

```bash
./gradlew ktlintCheck
./gradlew ktlintFormat
```

### Dependency Management

The project uses dependency guard to prevent dependency drift:

```bash
./gradlew dependencyGuard
```

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

### Before Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Ensure all checks pass
6. Submit a pull request

### Code of Conduct

This project adheres to a [Code of Conduct](.github/CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code.

## 🔒 Security

We take security seriously. Please report security vulnerabilities to [volodymyr.nevmerzhytskyi@sdkforge.dev](mailto:volodymyr.nevmerzhytskyi@sdkforge.dev).

**Do NOT create public GitHub issues for security vulnerabilities.**

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Documentation**: Check our [documentation](https://github.com/SDKForge/template-sdk#readme)
- **Issues**: [GitHub Issues](https://github.com/SDKForge/template-sdk/issues)
- **Discussions**: [GitHub Discussions](https://github.com/orgs/SDKForge/discussions)
- **Email**: [volodymyr.nevmerzhytskyi@sdkforge.dev](mailto:volodymyr.nevmerzhytskyi@sdkforge.dev)

## 🗺️ Roadmap

- [ ] **Publishing Setup** - Configure Maven Central or other repository publishing
- [ ] **CI/CD Pipeline** - Automated publishing workflows
- [ ] **Web platform support** - Extend to web platforms
- [ ] **Desktop platform support** - Add desktop (Windows, macOS, Linux) support
- [ ] **Enhanced performance monitoring** - Advanced benchmarking and profiling
- [ ] **More UI components** - Additional Compose Multiplatform components
- [ ] **Advanced configuration options** - Flexible SDK configuration
- [ ] **Documentation site** - Dedicated documentation website

## 🙏 Acknowledgments

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) team
- [Jetpack Compose](https://developer.android.com/jetpack/compose) team
- [JetBrains](https://www.jetbrains.com/) for excellent tooling
- All contributors and community members

---

**Made with ❤️ by the SDKForge Team**
