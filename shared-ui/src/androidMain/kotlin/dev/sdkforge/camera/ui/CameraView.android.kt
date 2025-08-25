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
