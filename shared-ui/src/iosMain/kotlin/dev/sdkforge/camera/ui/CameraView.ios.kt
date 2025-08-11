@file:Suppress("ktlint:standard:class-signature")

package dev.sdkforge.camera.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.viewinterop.UIKitView

@Composable
actual fun CameraView(
    cameraController: CameraController,
    modifier: Modifier,
) {
    LaunchedEffect(cameraController.cameraState.hasCameraPermission) {
        if (cameraController.cameraState.hasCameraPermission) {
            cameraController.onPermissionGranted()
        }
    }

    UIKitView(
        factory = { cameraController.platformCameraView.uiView },
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
    val hasCameraPermission = hasCameraPermission
    val hapticFeedback = LocalHapticFeedback.current
    return remember(hasCameraPermission, config) {
        NativeCameraControllerImpl(
            initialCameraState = CameraStateImpl(
                hasCameraPermission = hasCameraPermission,
            ),
            config = config,
            hapticFeedback = hapticFeedback,
        )
    }
}
