package dev.sdkforge.camera.domain

/**
 * Configuration for camera behavior and scanning capabilities.
 *
 * This data class defines the settings that control how the camera operates,
 * including supported barcode formats, camera features, and user preferences.
 *
 * @property formats Set of supported barcode formats for scanning. Defaults to all available formats.
 * @property enableTorch Whether to enable the camera flash/torch functionality. Defaults to false.
 * @property useHapticFeedback Whether to provide haptic feedback when a barcode is scanned. Defaults to false.
 * @property cameraFacing Which camera lens to use (front or back). Defaults to back camera.
 */
data class CameraConfig(
    /**
     * Set of supported barcode formats for scanning.
     *
     * By default, all available formats are enabled. You can customize this
     * to limit scanning to specific barcode types for better performance
     * or to match your application's requirements.
     */
    val formats: Set<Format> = Format.entries.toSet(),

    /**
     * Whether to enable the camera flash/torch functionality.
     *
     * When enabled, users can toggle the camera flash to improve scanning
     * in low-light conditions.
     */
    val enableTorch: Boolean = false,

    /**
     * Whether to provide haptic feedback when a barcode is scanned.
     *
     * When enabled, the device will vibrate briefly when a barcode is
     * successfully detected and processed.
     */
    val useHapticFeedback: Boolean = false,

    /**
     * Which camera lens to use for scanning.
     *
     * Choose between front-facing camera (selfie camera) or back-facing
     * camera (main camera). Back camera is typically preferred for barcode
     * scanning due to better image quality.
     */
    val cameraFacing: Facing = Facing.BACK,
)
