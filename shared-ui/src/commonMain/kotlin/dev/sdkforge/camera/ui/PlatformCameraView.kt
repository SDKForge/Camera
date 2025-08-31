package dev.sdkforge.camera.ui

/**
 * Platform-specific camera view implementation.
 *
 * This expect class is implemented differently for each target platform
 * to provide platform-specific camera functionality. It handles camera
 * lifecycle events and permission management.
 *
 * ## Platform Implementations
 *
 * - **Android**: Uses Android CameraX API for camera functionality
 * - **iOS**: Uses AVFoundation framework for camera functionality
 */
internal expect class PlatformCameraView {

    /**
     * Handles camera permission being granted.
     *
     * Called when the user grants camera permission. This method should
     * initialize the camera and start the preview if not already active.
     */
    internal fun onPermissionGranted()

    /**
     * Initializes the camera view.
     *
     * Called to set up the camera view and prepare it for use.
     * This method should configure the camera preview and scanning capabilities.
     */
    internal fun onInit()

    /**
     * Resets the camera view to its initial state.
     *
     * Called to reset the camera view, typically after an error or
     * when changing camera settings. This method should clear any
     * error states and prepare for reinitialization.
     */
    internal fun onReset()

    /**
     * Releases camera resources.
     *
     * Called when the camera view is no longer needed. This method should
     * properly clean up camera resources and stop the preview.
     */
    internal fun onRelease()
}
