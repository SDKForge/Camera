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

internal actual class PlatformCameraView(
    context: Context,
    private val config: CameraConfig,
    private val lifecycleOwner: LifecycleOwner,
    private val onScan: (result: ScanResult) -> Unit,
) {
    private val controller: LifecycleCameraController = LifecycleCameraController(context)
    private val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    internal val previewView: PreviewView = PreviewView(context).apply {
        setBackgroundColor(Color.TRANSPARENT)
        scaleType = PreviewView.ScaleType.FILL_CENTER
        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
    }

    internal actual fun onPermissionGranted() {
        controller.bindToLifecycle(lifecycleOwner)
        previewView.controller = controller
        previewView.invalidate()

        onInit()
    }

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

    internal actual fun onReset() {
    }

    internal actual fun onRelease() {
        controller.unbind()
    }
}
