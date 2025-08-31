package dev.sdkforge.camera.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.sdkforge.camera.domain.CameraConfig

/**
 * Android implementation of the camera view composable.
 *
 * This actual implementation provides Android-specific camera rendering using
 * AndroidView to integrate with the native Android camera preview.
 *
 * @param cameraController The camera controller that manages camera state and scanning
 * @param modifier Optional modifier to apply to the camera view
 */
@Composable
actual fun CameraView(
    cameraController: CameraController,
    modifier: Modifier,
) {
    val nativeCameraController: NativeCameraController? = cameraController as? NativeCameraController

    LaunchedEffect(cameraController.cameraState.hasCameraPermission) {
        if (cameraController.cameraState.hasCameraPermission) {
            nativeCameraController?.onPermissionGranted()
        }
    }

    val hasCameraPermission = hasCameraPermission

    LifecycleResumeEffect(hasCameraPermission) {
        nativeCameraController?.initialCameraState?.hasCameraPermission = hasCameraPermission

        onPauseOrDispose {}
    }

    AndroidView(
        factory = { cameraController.platformCameraView.previewView },
        modifier = modifier,
        onReset = {
            cameraController.onReset()
        },
        onRelease = {
            cameraController.onRelease()
        },
    )
}

/**
 * Android implementation of the camera controller factory.
 *
 * This actual implementation creates an Android-specific camera controller
 * using the Android context, haptic feedback, and lifecycle owner.
 *
 * @param config The camera configuration settings
 * @return A [CameraController] instance configured for Android
 */
@Composable
actual fun rememberCameraController(
    config: CameraConfig,
): CameraController {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val hasCameraPermission = hasCameraPermission

    return remember(hasCameraPermission, config) {
        NativeCameraControllerImpl(
            context = context,
            config = config,
            initialCameraState = CameraStateImpl(
                hasCameraPermission = hasCameraPermission,
            ),
            hapticFeedback = hapticFeedback,
            lifecycleOwner = lifecycleOwner,
        )
    }
}
