package dev.sdkforge.buildlogic

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Gradle convention plugin for Android library projects in Kotlin Multiplatform builds.
 *
 * This plugin provides standardized configuration for Android library modules, including
 * SDK configuration, Java compatibility, Kotlin compiler options, and comprehensive
 * lint analysis. It's designed to work seamlessly with Kotlin Multiplatform projects.
 *
 * ## Features
 *
 * - **Android Library Setup**: Automatically applies the Android library plugin
 * - **SDK Configuration**: Sets compile and target SDK versions
 * - **Java Compatibility**: Configures Java 17 compatibility
 * - **Kotlin Configuration**: Sets JVM target and compiler options
 * - **Comprehensive Linting**: Full Android lint analysis with multiple report formats
 * - **Multi-format Reports**: HTML, XML, SARIF, and text reports
 * - **Test Source Analysis**: Includes test sources in lint analysis
 * - **Dependency Checking**: Analyzes dependencies for potential issues
 *
 * ## Usage
 *
 * ```kotlin
 * plugins {
 *     id("dev.sdkforge.buildlogic.library.android")
 * }
 * ```
 *
 * ## Configuration
 *
 * ### SDK Configuration
 * - **Compile SDK**: 36 (Android 14)
 * - **Minimum SDK**: 23 (Android 6.0 Marshmallow)
 * - **Target SDK**: 36 (Android 14)
 *
 * ### Java Configuration
 * - **Source Compatibility**: Java 17
 * - **Target Compatibility**: Java 17
 *
 * ### Kotlin Configuration
 * - **JVM Target**: Java 17
 * - **Warnings as Errors**: Disabled
 *
 * ## Lint Configuration
 *
 * The plugin provides comprehensive lint analysis with the following features:
 *
 * ### Report Formats
 * - **HTML Report**: `build/reports/lint-results-debug.html`
 * - **XML Report**: `build/reports/lint-results-debug.xml`
 * - **SARIF Report**: `build/reports/lint-results-debug.sarif`
 * - **Text Report**: `build/reports/lint-results-debug.txt`
 *
 * ### Analysis Options
 * - **Quiet Mode**: Disabled (shows progress)
 * - **Abort on Error**: Enabled (fails build on errors)
 * - **Check Release Builds**: Enabled
 * - **Check Test Sources**: Enabled
 * - **Check Generated Sources**: Enabled
 * - **Check Dependencies**: Enabled
 *
 * ### Issue Severity
 * - **Fatal Issues**: Customizable list
 * - **Error Issues**: Customizable list
 * - **Warning Issues**: Customizable list
 * - **Ignore Issues**: Customizable list
 * - **Informational Issues**: Customizable list; includes "UseTomlInstead" and "NewerVersionAvailable"

 * ## Dependencies
 *
 * This plugin requires:
 * - Android Gradle Plugin
 * - Kotlin Android Plugin
 * - Android SDK with API level 36
 * - Java 17 or later
 *
 * ## Reports Location
 *
 * All lint reports are generated in:
 * ```
 * build/reports/lint-results-{variant}/
 * ├── lint-results-{variant}.html
 * ├── lint-results-{variant}.xml
 * ├── lint-results-{variant}.sarif
 * └── lint-results-{variant}.txt
 * ```
 *
 * @see [Android Lint Documentation](https://developer.android.com/studio/write/lint)
 * @see [Android Gradle Plugin](https://developer.android.com/studio/build)
 * @see [Kotlin Android Plugin](https://kotlinlang.org/docs/android-overview.html)
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            configure<LibraryExtension> {
                configureSDK()      // Set SDK versions
                configureJava()     // Configure Java compatibility
                configureLint()     // Set up lint analysis
            }

            // Configure Kotlin compiler options
            configureKotlin()
        }
    }
}

/**
 * Configures Android SDK versions for the library.
 *
 * Sets the compile SDK to 36 (Android 14) and minimum SDK to 23 (Android 6.0).
 */
private fun CommonExtension<*, *, *, *, *, *>.configureSDK() {
    compileSdk = 36  // Android 14 (API level 36)

    defaultConfig {
        minSdk = 21  // Android 5.0 Lollipop (API level 21)
    }
}

/**
 * Configures Java compatibility settings.
 *
 * Sets both source and target compatibility to Java 17, ensuring consistent
 * bytecode generation and compatibility across different Java versions.
 */
private fun CommonExtension<*, *, *, *, *, *>.configureJava() {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

/**
 * Configures Kotlin compiler options for the project.
 *
 * Sets the JVM target to Java 17 and configures warning handling.
 */
private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)           // Target Java 17 bytecode
            allWarningsAsErrors.set(false)            // Don't treat warnings as errors
        }
    }
}

/**
 * Configures comprehensive Android lint analysis.
 *
 * Sets up lint with multiple report formats, comprehensive issue checking,
 * and integration with the build process. Includes test sources and dependencies
 * in analysis for thorough code quality assessment.
 */
private fun LibraryExtension.configureLint() {
    lint {
        // Analysis behavior configuration
        quiet = false                    // Show analysis progress
        abortOnError = true              // Fail build on lint errors
        checkReleaseBuilds = true        // Run lint on release builds

        // Issue reporting configuration
        ignoreWarnings = false           // Report all warnings
        absolutePaths = true             // Use absolute file paths
        checkAllWarnings = true          // Check all issue types
        warningsAsErrors = true          // Treat warnings as errors

        // Issue filtering (customizable)
        disable += listOf()              // Disable specific issues
        enable += listOf()               // Enable specific issues
        checkOnly += listOf()            // Check only specific issues

        // Output configuration
        noLines = true                   // Don't include source lines in output
        showAll = true                   // Show all error locations
        explainIssues = true             // Include issue explanations

        // Report generation
        textReport = true                // Generate text report
        xmlReport = true                 // Generate XML report (CI integration)
        htmlReport = true                // Generate HTML report (human readable)
        sarifReport = true               // Generate SARIF report (tool integration)

        // Issue severity configuration
        fatal += listOf()                // Custom fatal issues
        error += listOf()                // Custom error issues
        warning += listOf()              // Custom warning issues
        ignore += listOf()               // Custom ignored issues
        informational += listOf(
            "UseTomlInstead",            // Custom informational issues
            "NewerVersionAvailable",
        )

        // Source analysis configuration
        checkTestSources = true          // Include test sources in analysis
        ignoreTestSources = false        // Don't skip test source analysis
        checkGeneratedSources = true     // Include generated sources
        checkDependencies = true         // Analyze dependencies for issues

        // Library-specific configuration
        targetSdk = 36                   // Target SDK for lint analysis (must be >= compile SDK)
    }
}
