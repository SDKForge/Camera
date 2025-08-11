package dev.sdkforge.camera.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

internal actual val hasCameraPermission: Boolean
    @Composable get() = ContextCompat.checkSelfPermission(
        LocalContext.current,
        Manifest.permission.CAMERA,
    ) == PackageManager.PERMISSION_GRANTED
