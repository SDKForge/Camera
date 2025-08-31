package dev.sdkforge.camera.ui

import androidx.compose.ui.hapticfeedback.HapticFeedback
import dev.sdkforge.camera.domain.CameraConfig

/**
 * iOS implementation of the native camera controller.
 *
 * This actual class provides iOS-specific camera controller functionality
 * using the iOS haptic feedback system.
 *
 * @property initialCameraState The initial camera state with permission information
 * @property config The camera configuration settings
 * @property hapticFeedback The iOS haptic feedback system
 */
internal actual class NativeCameraControllerImpl(
    /**
     * The initial camera state implementation.
     *
     * Provides the concrete implementation of camera state that can be
     * modified by the controller.
     */
    actual override val initialCameraState: CameraStateImpl,

    /**
     * The camera configuration settings.
     *
     * Contains all the settings that control camera behavior, including
     * supported formats, camera facing, and user preferences.
     */
    config: CameraConfig,

    /**
     * The iOS haptic feedback system.
     *
     * Used to provide tactile feedback when barcodes are scanned,
     * if enabled in the configuration.
     */
    hapticFeedback: HapticFeedback,
) : NativeCameraController(config, hapticFeedback) {

    /**
     * The iOS platform camera view implementation.
     *
     * Provides iOS-specific camera functionality using AVFoundation
     * for barcode scanning.
     */
    actual override val platformCameraView: PlatformCameraView = PlatformCameraView(config, ::onScan)
}
