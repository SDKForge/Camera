package dev.sdkforge.camera.core

interface Platform {
    val name: String
    val version: String
}

expect val currentPlatform: Platform
