package dev.sdkforge.camera.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.sdkforge.camera.domain.CameraConfig

@Composable
expect fun CameraView(
    cameraController: CameraController,
    modifier: Modifier = Modifier,
)

@Composable
expect fun rememberCameraController(
    config: CameraConfig = CameraConfig(),
): CameraController
