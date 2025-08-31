package dev.sdkforge.camera.ui

import androidx.compose.runtime.Composable

/**
 * Platform-specific camera permission state.
 *
 * This expect property is implemented differently for each target platform
 * to provide accurate camera permission status. It indicates whether the
 * application has been granted camera permission by the user.
 *
 * ## Platform Implementations
 *
 * - **Android**: Checks Android camera permission status
 * - **iOS**: Checks iOS camera permission status
 *
 * @return true if camera permission is granted, false otherwise
 */
internal expect val hasCameraPermission: Boolean
    @Composable get
