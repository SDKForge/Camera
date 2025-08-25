@file:Suppress("ktlint:standard:class-signature")

package dev.sdkforge.camera.domain

data class ScanResult(
    val format: Format,
    val value: String?,
)
