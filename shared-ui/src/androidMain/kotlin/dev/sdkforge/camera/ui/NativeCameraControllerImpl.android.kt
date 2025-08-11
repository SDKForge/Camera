package dev.sdkforge.camera.ui

import android.content.Context
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.lifecycle.LifecycleOwner

internal actual class NativeCameraControllerImpl(
    context: Context,
    config: CameraConfig,
    actual override val initialCameraState: CameraStateImpl,
    hapticFeedback: HapticFeedback,
    lifecycleOwner: LifecycleOwner,
) : NativeCameraController(config, hapticFeedback) {

    actual override val platformCameraView: PlatformCameraView = PlatformCameraView(context, config, lifecycleOwner, ::onScan)
}
