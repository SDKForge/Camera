@file:Suppress("ktlint:standard:class-signature")

package dev.sdkforge.camera.ui

import androidx.compose.runtime.Stable

/**
 * Interface representing the current state of the camera.
 *
 * This interface provides information about the camera's current status,
 * including permission state and other relevant information that may be
 * needed by the UI layer.
 */
interface CameraState {
    /**
     * Whether the application has camera permission.
     *
     * Indicates whether the user has granted camera permission to the application.
     * This affects whether the camera can be used for scanning.
     */
    val hasCameraPermission: Boolean
}

/**
 * Stable implementation of camera state.
 *
 * This data class provides a concrete implementation of [CameraState] that
 * can be modified by camera controllers. The [@Stable] annotation indicates
 * that this class is stable for Compose optimization purposes.
 *
 * @property hasCameraPermission Whether the application has camera permission
 */
@Stable
internal data class CameraStateImpl(
    /**
     * Whether the application has camera permission.
     *
     * Indicates whether the user has granted camera permission to the application.
     * This affects whether the camera can be used for scanning.
     */
    override var hasCameraPermission: Boolean,
) : CameraState
