package dev.sdkforge.camera.ui

internal expect class PlatformCameraView {

    internal fun onPermissionGranted()
    internal fun onInit()
    internal fun onReset()
    internal fun onRelease()
}
