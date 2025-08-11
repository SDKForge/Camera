@file:Suppress("ktlint:standard:class-signature")

package dev.sdkforge.camera.ui

import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal expect class NativeCameraControllerImpl : NativeCameraController {

    override val initialCameraState: CameraStateImpl

    override val platformCameraView: PlatformCameraView
}

internal abstract class NativeCameraController(
    protected val config: CameraConfig,
    protected val hapticFeedback: HapticFeedback,
) : CameraController() {

    protected val initialScannedResults: MutableSharedFlow<ScanResult> = MutableSharedFlow()
    override val scannedResults: Flow<ScanResult> = initialScannedResults

    internal abstract val initialCameraState: CameraStateImpl
    override val cameraState: CameraState get() = initialCameraState

    abstract override val platformCameraView: PlatformCameraView

    @Suppress("ktlint:standard:function-signature")
    internal fun onScan(result: ScanResult) {
        if (initialScannedResults.tryEmit(result) && config.useHapticFeedback) {
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.Confirm)
        }
    }

    override fun onPermissionGranted() {
        platformCameraView.onPermissionGranted()
    }

    override fun onReset() {
        platformCameraView.onReset()
        platformCameraView.onInit()
    }

    override fun onRelease() {
        platformCameraView.onRelease()
    }
}

abstract class CameraController {
    abstract val cameraState: CameraState

    abstract val scannedResults: Flow<ScanResult>

    internal abstract val platformCameraView: PlatformCameraView

    internal abstract fun onPermissionGranted()
    internal abstract fun onReset()
    internal abstract fun onRelease()
}
