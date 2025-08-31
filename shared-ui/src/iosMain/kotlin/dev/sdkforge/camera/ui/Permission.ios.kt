package dev.sdkforge.camera.ui

import androidx.compose.runtime.Composable
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType

/**
 * iOS implementation of camera permission checking.
 *
 * This actual implementation uses iOS AVFoundation framework to check
 * if the application has been granted camera permission by the user.
 *
 * ## Implementation Details
 *
 * Uses `AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)` to check
 * the camera authorization status and returns true only for `AVAuthorizationStatusAuthorized`.
 *
 * ## Permission States
 *
 * - **Authorized**: Permission granted, returns true
 * - **Denied**: Permission denied by user, returns false
 * - **Restricted**: Permission restricted by system, returns false
 * - **Not Determined**: Permission not yet requested, returns false
 *
 * @return true if camera permission is granted, false otherwise
 */
internal actual val hasCameraPermission: Boolean
    @Composable get() = when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
        AVAuthorizationStatusAuthorized -> true
        AVAuthorizationStatusDenied -> false
        AVAuthorizationStatusRestricted -> false
        AVAuthorizationStatusNotDetermined -> false
        else -> false
    }
