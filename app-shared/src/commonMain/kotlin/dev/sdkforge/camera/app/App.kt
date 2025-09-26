package dev.sdkforge.camera.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.sdkforge.camera.domain.ScanResult
import dev.sdkforge.camera.ui.CameraController

@Composable
fun App(
    cameraController: CameraController,
    scans: Set<ScanResult>,
    modifier: Modifier = Modifier,
) = ApplicationTheme {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
    ) {
        ScannerScreen(
            cameraController = cameraController,
            scans = scans,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

object AppConstants {
    const val HISTORY_SCANS_MAX_LENGTH = 5
    const val ERROR_TITLE = "Error"
    const val SUCCESS_TITLE = "Success"
    const val ALREADY_SCANNED_MESSAGE = "Already scanned, check scans history"
    const val SCANNED_VALUE = "Scanned value:"
}
