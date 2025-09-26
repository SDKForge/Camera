package dev.sdkforge.camera.android

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.sdkforge.camera.app.App
import dev.sdkforge.camera.app.AppConstants
import dev.sdkforge.camera.domain.CameraConfig
import dev.sdkforge.camera.domain.Facing
import dev.sdkforge.camera.domain.Format
import dev.sdkforge.camera.domain.ScanResult
import dev.sdkforge.camera.ui.rememberCameraController

class MainActivity : ComponentActivity() {
    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current

            fun toast(
                text: String,
            ) {
                Toast.makeText(context, text, LENGTH_SHORT).show()
            }

            val requestPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
            ) { isGranted ->
                if (isGranted) {
                    toast("Permission is granted!")
                } else {
                    toast("Permission is not granted!")
                }
            }

            val cameraController = rememberCameraController(
                config = CameraConfig(
                    formats = setOf(Format.QR_CODE),
                    enableTorch = false,
                    useHapticFeedback = true,
                    cameraFacing = Facing.BACK,
                ),
            )
            val scans = remember { mutableStateSetOf<ScanResult>() }
            App(
                cameraController = cameraController,
                scans = scans,
                modifier = Modifier
                    .fillMaxSize(),
            )

            LaunchedEffect(Unit) {
                cameraController.scannedResults.collect { scanResult ->
                    if (scans.contains(scanResult)) {
                        toast("${AppConstants.SUCCESS_TITLE} - ${AppConstants.ALREADY_SCANNED_MESSAGE}")
                    } else {
                        if (scans.size == AppConstants.HISTORY_SCANS_MAX_LENGTH) {
                            scans.remove(scans.first())
                        }
                        scans.add(scanResult)
                        toast("${AppConstants.SUCCESS_TITLE} - ${AppConstants.SCANNED_VALUE} ${scanResult.value}")
                    }
                }
            }

            LaunchedEffect(cameraController.cameraState.hasCameraPermission) {
                if (!cameraController.cameraState.hasCameraPermission) {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        }
    }
}
