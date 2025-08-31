package dev.sdkforge.camera.domain

/**
 * Camera lens facing direction.
 *
 * This enum defines the available camera lenses that can be used
 * for barcode scanning. The choice of camera affects image quality
 * and scanning performance.
 */
enum class Facing {
    /**
     * Front-facing camera (selfie camera).
     *
     * Typically used for self-portraits and video calls. May have
     * lower resolution and image quality compared to the back camera.
     */
    FRONT,

    /**
     * Back-facing camera (main camera).
     *
     * The primary camera with higher resolution and better image quality.
     * Recommended for barcode scanning due to superior optics and
     * autofocus capabilities.
     */
    BACK,
}
