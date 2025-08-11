@file:Suppress("ktlint:standard:class-signature")

package dev.sdkforge.camera.ui

import androidx.compose.runtime.Stable

interface CameraState {
    val hasCameraPermission: Boolean
}

@Stable
internal data class CameraStateImpl(
    override var hasCameraPermission: Boolean,
) : CameraState
