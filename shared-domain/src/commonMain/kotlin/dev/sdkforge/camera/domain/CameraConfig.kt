package dev.sdkforge.camera.domain

data class CameraConfig(
    val formats: Set<Format> = Format.entries.toSet(),
    val enableTorch: Boolean = false,
    val useHapticFeedback: Boolean = false,
    val cameraFacing: Facing = Facing.BACK,
)
