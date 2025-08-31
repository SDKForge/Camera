@file:Suppress("ktlint:standard:class-signature")

package dev.sdkforge.camera.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.viewinterop.UIKitView
import dev.sdkforge.camera.domain.CameraConfig

/**
 * iOS implementation of the camera view composable.
 *
 * This actual implementation provides iOS-specific camera rendering using
 * UIKitView to integrate with the native iOS camera preview.
 *
 * @param cameraController The camera controller that manages camera state and scanning
 * @param modifier Optional modifier to apply to the camera view
 */
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

/**
 * iOS implementation of the camera controller factory.
 *
 * This actual implementation creates an iOS-specific camera controller
 * using the iOS haptic feedback system.
 *
 * @param config The camera configuration settings
 * @return A [CameraController] instance configured for iOS
 */
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
