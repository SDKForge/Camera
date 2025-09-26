@file:Suppress("ktlint:standard:class-signature", "ktlint:standard:function-expression-body")

package dev.sdkforge.camera.ui

import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import dev.sdkforge.camera.domain.CameraConfig
import dev.sdkforge.camera.domain.ScanResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Platform-specific implementation of the native camera controller.
 *
 * This expect class is implemented differently for each target platform
 * to provide platform-specific camera functionality.
 *
 * @property initialCameraState The initial state of the camera when the controller is created
 * @property platformCameraView The platform-specific camera view implementation
 */
internal expect class NativeCameraControllerImpl : NativeCameraController {

    override val initialCameraState: CameraStateImpl

    override val platformCameraView: PlatformCameraView
}

/**
 * Abstract base class for native camera controller implementations.
 *
 * This class provides the core functionality for camera control, including
 * barcode scanning, haptic feedback, and camera state management. It serves
 * as the foundation for platform-specific implementations.
 *
 * @property config The camera configuration settings
 * @property hapticFeedback The haptic feedback system for providing tactile responses
 */
internal abstract class NativeCameraController(
    /**
     * The camera configuration settings.
     *
     * Contains all the settings that control camera behavior, including
     * supported formats, camera facing, and user preferences.
     */
    protected val config: CameraConfig,

    /**
     * The haptic feedback system.
     *
     * Used to provide tactile feedback when barcodes are scanned,
     * if enabled in the configuration.
     */
    protected val hapticFeedback: HapticFeedback,
) : CameraController() {

    /**
     * Internal flow for scanned results.
     *
     * This mutable shared flow is used internally to emit scan results
     * and can be shared with multiple collectors.
     */
    protected val initialScannedResults: MutableSharedFlow<ScanResult> = MutableSharedFlow(replay = 1)

    /**
     * Public flow of scanned results.
     *
     * This flow emits [ScanResult] objects whenever a barcode is successfully
     * scanned and decoded.
     */
    override val scannedResults: SharedFlow<ScanResult> = initialScannedResults

    /**
     * The initial camera state implementation.
     *
     * Provides the concrete implementation of camera state that can be
     * modified by the controller.
     */
    internal abstract val initialCameraState: CameraStateImpl

    /**
     * The current camera state.
     *
     * Returns the initial camera state implementation as the public interface.
     */
    override val cameraState: CameraState get() = initialCameraState

    /**
     * The platform-specific camera view.
     *
     * Provides access to platform-specific camera view functionality
     * for permission handling and lifecycle management.
     */
    abstract override val platformCameraView: PlatformCameraView

    /**
     * Handles a successful barcode scan.
     *
     * This method is called internally when a barcode is detected and decoded.
     * It emits the result to the scanned results flow and provides haptic feedback
     * if enabled in the configuration.
     *
     * @param result The scan result containing the barcode format and decoded value
     */
    @Suppress("ktlint:standard:function-signature")
    internal fun onScan(result: ScanResult) {
        if (initialScannedResults.tryEmit(result) && config.useHapticFeedback) {
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.Confirm)
        }
    }

    /**
     * Handles camera permission being granted.
     *
     * Called when the user grants camera permission. This method delegates
     * to the platform camera view to handle the permission change.
     */
    override fun onPermissionGranted() {
        platformCameraView.onPermissionGranted()
    }

    /**
     * Resets the camera to its initial state.
     *
     * This method resets the platform camera view and reinitializes it.
     * Useful for recovering from errors or changing camera settings.
     */
    override fun onReset() {
        platformCameraView.onReset()
        platformCameraView.onInit()
    }

    /**
     * Releases camera resources.
     *
     * Called when the camera controller is no longer needed. This method
     * delegates to the platform camera view to properly clean up resources.
     */
    override fun onRelease() {
        platformCameraView.onRelease()
    }

    override fun toggleFlash() {
        platformCameraView.toggleFlash()
    }

    override fun isFlashIsOn(): Boolean {
        return platformCameraView.isFlashIsOn()
    }

    override fun toggleActiveCamera() {
        platformCameraView.toggleActiveCamera()
    }
}

/**
 * Abstract base class for camera controllers.
 *
 * This class defines the interface for camera controllers that manage
 * camera state, scanning results, and platform-specific camera views.
 * It provides a common interface for different camera implementations.
 */
abstract class CameraController {
    /**
     * The current camera state.
     *
     * Provides information about the camera's current status, including
     * permission state and other relevant information.
     */
    abstract val cameraState: CameraState

    /**
     * Flow of scanned barcode results.
     *
     * Emits [ScanResult] objects whenever a barcode is successfully
     * scanned and decoded by the camera.
     */
    abstract val scannedResults: Flow<ScanResult>

    /**
     * The platform-specific camera view.
     *
     * Provides access to platform-specific camera view functionality
     * for internal operations like permission handling and lifecycle management.
     */
    internal abstract val platformCameraView: PlatformCameraView

    /**
     * Handles camera permission being granted.
     *
     * Called internally when the user grants camera permission.
     * Implementations should update the camera state accordingly.
     */
    internal abstract fun onPermissionGranted()

    /**
     * Resets the camera to its initial state.
     *
     * Called internally to reset the camera. Implementations should
     * reinitialize the camera and clear any error states.
     */
    internal abstract fun onReset()

    /**
     * Releases camera resources.
     *
     * Called internally when the camera controller is being destroyed.
     * Implementations should properly clean up camera resources.
     */
    internal abstract fun onRelease()

    /**
     * Changes state of camera flash to opposite of current.
     *
     * Provides control of flash in torch mode only.
     */
    abstract fun toggleFlash()

    /**
     * Check for flash is currently on in torch mode.
     */
    abstract fun isFlashIsOn(): Boolean

    /**
     * Changes what camera is active at the moment.
     *
     * Provides control of what camera, frontal or back, is currently active.
     */
    abstract fun toggleActiveCamera()
}
