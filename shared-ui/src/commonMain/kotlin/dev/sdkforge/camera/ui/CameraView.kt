package dev.sdkforge.camera.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.sdkforge.camera.domain.CameraConfig

/**
 * Composable function that renders a camera view for barcode scanning.
 *
 * This expect function is implemented differently for each target platform
 * to provide platform-specific camera rendering. The camera view displays
 * the camera preview and handles user interactions for barcode scanning.
 *
 * @param cameraController The camera controller that manages camera state and scanning
 * @param modifier Optional modifier to apply to the camera view
 */
@Composable
expect fun CameraView(
    cameraController: CameraController,
    modifier: Modifier = Modifier,
)

/**
 * Creates and remembers a camera controller instance.
 *
 * This expect function is implemented differently for each target platform
 * to provide platform-specific camera controller implementations. The controller
 * manages camera state, scanning results, and platform-specific camera functionality.
 *
 * @param config The camera configuration settings. Defaults to a basic configuration
 *               with all formats enabled and back camera facing.
 * @return A [CameraController] instance that can be used with [CameraView]
 */
@Composable
expect fun rememberCameraController(
    config: CameraConfig = CameraConfig(),
): CameraController
