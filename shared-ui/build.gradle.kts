plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.binaryCompatibilityValidator)
    alias(libs.plugins.dokka)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.build.logic.library.kmp)
    alias(libs.plugins.build.logic.library.android)
    alias(libs.plugins.build.logic.library.publishing)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.sharedDomain)

                implementation(compose.foundation)
                implementation(compose.material3)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation("androidx.camera:camera-camera2:1.4.2")
                implementation("androidx.camera:camera-view:1.4.2")
                implementation("com.google.mlkit:barcode-scanning:17.3.0")
                implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.1")
            }
        }
    }
}

android {
    namespace = "dev.sdkforge.camera.ui"
}
