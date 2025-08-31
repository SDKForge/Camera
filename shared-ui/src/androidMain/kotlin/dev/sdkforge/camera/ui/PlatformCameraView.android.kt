package dev.sdkforge.camera.ui

import android.content.Context
import android.graphics.Color
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dev.sdkforge.camera.domain.CameraConfig
import dev.sdkforge.camera.domain.Facing
import dev.sdkforge.camera.domain.ScanResult
import java.util.concurrent.Executor

/**
 * Android implementation of the platform camera view.
 *
 * This actual class provides Android-specific camera functionality using
 * CameraX for camera management and ML Kit for barcode scanning.
 *
 * ## Implementation Details
 *
 * - Uses CameraX `LifecycleCameraController` for camera lifecycle management
 * - Uses ML Kit `BarcodeScanning` for barcode detection and decoding
 * - Integrates with Android's permission system and lifecycle management
 *
 * @property context The Android application context
 * @property config The camera configuration settings
 * @property lifecycleOwner The Android lifecycle owner for camera lifecycle management
 * @property onScan Callback function called when a barcode is successfully scanned
 */
internal actual class PlatformCameraView(
    /**
     * The Android application context.
     *
     * Used to create camera components and access Android system services.
     */
    context: Context,

    /**
     * The camera configuration settings.
     *
     * Contains all the settings that control camera behavior, including
     * supported formats, camera facing, and user preferences.
     */
    private val config: CameraConfig,

    /**
     * The Android lifecycle owner.
     *
     * Used to bind camera functionality to the Android activity lifecycle
     * for proper resource management.
     */
    private val lifecycleOwner: LifecycleOwner,

    /**
     * Callback function for scan results.
     *
     * Called when a barcode is successfully detected and decoded.
     * Receives a [ScanResult] containing the format and decoded value.
     */
    private val onScan: (result: ScanResult) -> Unit,
) {
    /**
     * The CameraX camera controller.
     *
     * Manages camera lifecycle and provides camera functionality
     * using Android CameraX API.
     */
    private val controller: LifecycleCameraController = LifecycleCameraController(context)

    /**
     * The main executor for camera operations.
     *
     * Used to execute camera operations on the main thread
     * as required by CameraX.
     */
    private val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    /**
     * The camera preview view.
     *
     * Displays the camera preview and handles user interactions.
     * Configured with transparent background and fill-center scaling.
     */
    internal val previewView: PreviewView = PreviewView(context).apply {
        setBackgroundColor(Color.TRANSPARENT)
        scaleType = PreviewView.ScaleType.FILL_CENTER
        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
    }

    /**
     * Handles camera permission being granted.
     *
     * Binds the camera controller to the lifecycle and sets up
     * the preview view for camera display.
     */
    internal actual fun onPermissionGranted() {
        controller.bindToLifecycle(lifecycleOwner)
        previewView.controller = controller
        previewView.invalidate()

        onInit()
    }

    /**
     * Initializes the camera view.
     *
     * Configures camera settings, barcode scanner, and image analysis
     * for barcode detection and decoding.
     */
    @OptIn(ExperimentalGetImage::class)
    internal actual fun onInit() {
        controller.enableTorch(config.enableTorch)
        controller.cameraSelector = when (config.cameraFacing) {
            Facing.FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
            Facing.BACK -> CameraSelector.DEFAULT_BACK_CAMERA
        }

        val barcodeScannerOptions = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(config.formats.toBarcodeScannerOptions() ?: Barcode.FORMAT_ALL_FORMATS)
            .build()

        val scanner = BarcodeScanning.getClient(barcodeScannerOptions)

        controller.setImageAnalysisAnalyzer(mainExecutor) { imageProxy ->
            imageProxy.image?.let { image ->
                scanner.process(InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees))
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            val barcodeFormat = barcode.format.asFormat
                            if (barcodeFormat in config.formats) {
                                onScan.invoke(
                                    ScanResult(
                                        format = barcodeFormat,
                                        value = barcode.rawValue,
                                    ),
                                )
                            }
                        }
                    }.addOnCompleteListener {
                        imageProxy.close()
                    }
            }
        }
    }

    /**
     * Resets the camera view to its initial state.
     *
     * Currently a no-op implementation. Could be extended to reset
     * camera settings or clear error states.
     */
    internal actual fun onReset() {
    }

    /**
     * Releases camera resources.
     *
     * Unbinds the camera controller from the lifecycle to properly
     * clean up camera resources and stop the preview.
     */
    internal actual fun onRelease() {
        controller.unbind()
    }
}
