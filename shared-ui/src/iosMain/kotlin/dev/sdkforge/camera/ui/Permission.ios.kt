package dev.sdkforge.camera.ui

import androidx.compose.runtime.Composable
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType

internal actual val hasCameraPermission: Boolean
    @Composable get() = when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
        AVAuthorizationStatusAuthorized -> true
        AVAuthorizationStatusDenied -> false
        AVAuthorizationStatusRestricted -> false
        AVAuthorizationStatusNotDetermined -> false
        else -> false
    }
