package dev.sdkforge.camera.ui

import android.content.Context
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.lifecycle.LifecycleOwner
import dev.sdkforge.camera.domain.CameraConfig

/**
 * Android implementation of the native camera controller.
 *
 * This actual class provides Android-specific camera controller functionality
 * using the Android context, lifecycle owner, and haptic feedback system.
 *
 * @property context The Android application context
 * @property config The camera configuration settings
 * @property initialCameraState The initial camera state with permission information
 * @property hapticFeedback The Android haptic feedback system
 * @property lifecycleOwner The Android lifecycle owner for camera lifecycle management
 */
internal actual class NativeCameraControllerImpl(
    /**
     * The Android application context.
     *
     * Used to access Android system services and resources needed
     * for camera functionality.
     */
    context: Context,

    /**
     * The camera configuration settings.
     *
     * Contains all the settings that control camera behavior, including
     * supported formats, camera facing, and user preferences.
     */
    config: CameraConfig,

    /**
     * The initial camera state implementation.
     *
     * Provides the concrete implementation of camera state that can be
     * modified by the controller.
     */
    actual override val initialCameraState: CameraStateImpl,

    /**
     * The Android haptic feedback system.
     *
     * Used to provide tactile feedback when barcodes are scanned,
     * if enabled in the configuration.
     */
    hapticFeedback: HapticFeedback,

    /**
     * The Android lifecycle owner.
     *
     * Used to bind camera functionality to the Android activity lifecycle
     * for proper resource management.
     */
    lifecycleOwner: LifecycleOwner,
) : NativeCameraController(config, hapticFeedback) {

    /**
     * The Android platform camera view implementation.
     *
     * Provides Android-specific camera functionality using CameraX and ML Kit
     * for barcode scanning.
     */
    actual override val platformCameraView: PlatformCameraView = PlatformCameraView(context, config, lifecycleOwner, ::onScan)
}
