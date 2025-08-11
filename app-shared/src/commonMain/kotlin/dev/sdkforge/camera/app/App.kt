package dev.sdkforge.camera.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.sdkforge.camera.ui.CameraController
import dev.sdkforge.camera.ui.CameraView

@Composable
fun App(
    cameraController: CameraController,
    modifier: Modifier = Modifier,
) = ApplicationTheme {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
    ) {
        CameraView(
            cameraController = cameraController,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
