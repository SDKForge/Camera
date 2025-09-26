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
                implementation(libs.androidx.camera.camera)
                implementation(libs.androidx.camera.view)
                implementation(libs.barcode.scanning)
                implementation(libs.barcode.services)
            }
        }
    }
}

android {
    namespace = "dev.sdkforge.camera.ui"
}
