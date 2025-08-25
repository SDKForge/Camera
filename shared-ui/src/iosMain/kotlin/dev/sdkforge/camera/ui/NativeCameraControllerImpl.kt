package dev.sdkforge.camera.ui

import androidx.compose.ui.hapticfeedback.HapticFeedback
import dev.sdkforge.camera.domain.CameraConfig

internal actual class NativeCameraControllerImpl(
    actual override val initialCameraState: CameraStateImpl,
    config: CameraConfig,
    hapticFeedback: HapticFeedback,
) : NativeCameraController(config, hapticFeedback) {

    actual override val platformCameraView: PlatformCameraView = PlatformCameraView(config, ::onScan)
}
