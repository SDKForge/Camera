package dev.sdkforge.camera.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun CameraView(
    cameraController: CameraController,
    modifier: Modifier = Modifier,
)

@Composable
expect fun rememberCameraController(
    config: CameraConfig = CameraConfig(),
): CameraController

data class CameraConfig(
    val formats: Set<Format> = Format.entries.toSet(),
    val enableTorch: Boolean = false,
    val useHapticFeedback: Boolean = false,
    val cameraFacing: Facing = Facing.BACK,
)
