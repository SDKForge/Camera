import com.android.build.api.dsl.androidLibrary

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.build.logic.library.kmp)
    alias(libs.plugins.build.logic.library.android)
}

kotlin {
    androidLibrary {
        namespace = "dev.sdkforge.camera.app"
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared)

                api(compose.foundation)
                api(compose.material3)
            }
        }
    }
}
