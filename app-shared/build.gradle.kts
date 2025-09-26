plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.build.logic.library.kmp)
    alias(libs.plugins.build.logic.library.android)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared)

                api(compose.foundation)
                api(compose.material3)
                api(compose.materialIconsExtended)
            }
        }
    }
}

android {
    namespace = "dev.sdkforge.camera.app"
}
