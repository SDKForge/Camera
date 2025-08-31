package dev.sdkforge.camera.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * Android implementation of camera permission checking.
 *
 * This actual implementation uses Android's permission system to check
 * if the application has been granted camera permission by the user.
 *
 * ## Implementation Details
 *
 * Uses `ContextCompat.checkSelfPermission()` to check the camera permission
 * status against `Manifest.permission.CAMERA`.
 *
 * @return true if camera permission is granted, false otherwise
 */
internal actual val hasCameraPermission: Boolean
    @Composable get() = ContextCompat.checkSelfPermission(
        LocalContext.current,
        Manifest.permission.CAMERA,
    ) == PackageManager.PERMISSION_GRANTED
