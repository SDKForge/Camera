@file:Suppress("ktlint:standard:class-signature")

package dev.sdkforge.camera.ui

import dev.sdkforge.camera.domain.CameraConfig
import dev.sdkforge.camera.domain.Facing
import dev.sdkforge.camera.domain.ScanResult
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.AVFoundation.AVCaptureAutoFocusRangeRestrictionNear
import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureDevicePosition
import platform.AVFoundation.AVCaptureDevicePositionBack
import platform.AVFoundation.AVCaptureDevicePositionFront
import platform.AVFoundation.AVCaptureDevicePositionUnspecified
import platform.AVFoundation.AVCaptureDeviceType
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInTripleCamera
import platform.AVFoundation.AVCaptureInput
import platform.AVFoundation.AVCaptureMetadataOutput
import platform.AVFoundation.AVCaptureMetadataOutputObjectsDelegateProtocol
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureTorchModeAuto
import platform.AVFoundation.AVCaptureTorchModeOff
import platform.AVFoundation.AVCaptureTorchModeOn
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVMetadataMachineReadableCodeObject
import platform.AVFoundation.autoFocusRangeRestriction
import platform.AVFoundation.automaticallyEnablesLowLightBoostWhenAvailable
import platform.AVFoundation.defaultDeviceWithDeviceType
import platform.AVFoundation.isAutoFocusRangeRestrictionSupported
import platform.AVFoundation.isLowLightBoostSupported
import platform.AVFoundation.isTorchAvailable
import platform.AVFoundation.isTorchModeSupported
import platform.AVFoundation.position
import platform.AVFoundation.setFlashMode
import platform.AVFoundation.torchMode
import platform.CoreGraphics.CGRectZero
import platform.QuartzCore.CALayer
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

/**
 * iOS implementation of the platform camera view.
 *
 * This actual class provides iOS-specific camera functionality using
 * AVFoundation framework for camera management and barcode scanning.
 *
 * ## Implementation Details
 *
 * - Uses AVFoundation `AVCaptureSession` for camera lifecycle management
 * - Uses `AVCaptureMetadataOutput` for barcode detection and decoding
 * - Integrates with iOS permission system and UIKit for UI integration
 * - Supports torch/flash control and auto-focus configuration
 *
 * @property config The camera configuration settings
 * @property onScan Callback function called when a barcode is successfully scanned
 */
@OptIn(ExperimentalForeignApi::class)
internal actual class PlatformCameraView(
    /**
     * The camera configuration settings.
     *
     * Contains all the settings that control camera behavior, including
     * supported formats, camera facing, and user preferences.
     */
    private val config: CameraConfig,

    /**
     * Callback function for scan results.
     *
     * Called when a barcode is successfully detected and decoded.
     * Receives a [ScanResult] containing the format and decoded value.
     */
    private val onScan: (result: ScanResult) -> Unit,
) : NSObject(),
    AVCaptureMetadataOutputObjectsDelegateProtocol {

    /**
     * The UIKit view that displays the camera preview.
     *
     * Custom UIView that handles layer layout and provides the visual
     * container for the camera preview layer.
     */
    internal val uiView = object : UIView(frame = CGRectZero.readValue()) {
        // we should set layer layout size when view is measured
        override fun layoutSubviews() {
            super.layoutSubviews()
            for (sublayer in layer.sublayers.orEmpty()) {
                if (sublayer is CALayer) {
                    sublayer.setFrame(this.frame)
                }
            }
        }
    }

    /**
     * The AVFoundation capture session.
     *
     * Manages the camera input/output pipeline and coordinates
     * camera operations with the system.
     */
    internal val captureSession: AVCaptureSession = AVCaptureSession()

    /**
     * The video preview layer.
     *
     * Displays the camera preview and handles video rendering.
     * Added as a sublayer to the UIView for display.
     */
    internal val previewLayer: AVCaptureVideoPreviewLayer = AVCaptureVideoPreviewLayer().apply {
        uiView.layer.addSublayer(this)
    }

    /**
     * Configures camera device settings.
     *
     * Sets up auto-focus, low-light boost, and torch/flash settings
     * based on the camera configuration and device capabilities.
     *
     * @param captureDevice The camera device to configure
     */
    private fun configure(
        captureDevice: AVCaptureDevice,
    ) {
        captureDevice.lockForConfiguration(null)

        if (captureDevice.isAutoFocusRangeRestrictionSupported()) {
            captureDevice.autoFocusRangeRestriction = AVCaptureAutoFocusRangeRestrictionNear
        }
        if (captureDevice.isLowLightBoostSupported()) {
            captureDevice.automaticallyEnablesLowLightBoostWhenAvailable = true
        }
        when {
            config.enableTorch -> when {
                captureDevice.isTorchModeSupported(AVCaptureTorchModeOn) -> {
                    captureDevice.torchMode = AVCaptureTorchModeOn
                }

                captureDevice.isTorchModeSupported(AVCaptureTorchModeAuto) -> {
                    captureDevice.torchMode = AVCaptureTorchModeAuto
                }
            }

            else -> when {
                captureDevice.isTorchModeSupported(AVCaptureTorchModeOff) -> {
                    captureDevice.torchMode = AVCaptureTorchModeOff
                }
            }
        }

        captureDevice.unlockForConfiguration()
    }

    /**
     * Gets the preferred camera device.
     *
     * Selects the appropriate camera device based on the facing preference
     * (front or back camera) and device capabilities.
     *
     * @param preferFrontCamera Whether to prefer the front camera
     * @return The preferred camera device, or null if not available
     */
    private fun preferredCaptureDevice(
        preferFrontCamera: Boolean,
    ): AVCaptureDevice? {
        val preferredPosition = if (preferFrontCamera) AVCaptureDevicePositionFront else AVCaptureDevicePositionBack
        return AVCaptureDevice.defaultDeviceWithDeviceType(
            deviceType = AVCaptureDeviceTypeBuiltInTripleCamera,
            mediaType = AVMediaTypeVideo,
            position = preferredPosition,
        )
    }

    /**
     * Handles metadata output from the capture session.
     *
     * Called by AVFoundation when barcode metadata is detected.
     * Processes the detected barcodes and calls the scan callback
     * for valid formats.
     *
     * @param output The metadata output that generated the objects
     * @param didOutputMetadataObjects List of detected metadata objects
     * @param fromConnection The connection that generated the output
     */
    override fun captureOutput(
        output: AVCaptureOutput,
        didOutputMetadataObjects: List<*>,
        fromConnection: AVCaptureConnection,
    ) {
        for (metadata in didOutputMetadataObjects) {
            if (metadata !is AVMetadataMachineReadableCodeObject) continue

            if (metadata.type.asFormat in config.formats) {
                val barcode = previewLayer.transformedMetadataObjectForMetadataObject(
                    metadataObject = metadata,
                ) as? AVMetadataMachineReadableCodeObject

                onScan.invoke(
                    ScanResult(
                        format = (barcode?.type ?: metadata.type).asFormat,
                        value = barcode?.stringValue ?: metadata.stringValue,
                    ),
                )
            }
        }
    }

    /**
     * Handles camera permission being granted.
     *
     * Called when the user grants camera permission. This method
     * initializes the camera and starts the preview.
     */
    internal actual fun onPermissionGranted() {
        onInit()
    }

    /**
     * Initializes the camera view.
     *
     * Sets up the camera device, input/output pipeline, and metadata
     * processing for barcode detection and decoding.
     */
    internal actual fun onInit() {
        val captureDevice = preferredCaptureDevice(preferFrontCamera = config.cameraFacing == Facing.FRONT) ?: return
        val input = AVCaptureDeviceInput.deviceInputWithDevice(device = captureDevice, error = null) ?: return

        configure(captureDevice = captureDevice)

        captureSession.addInput(input)

        val captureMetadataOutput = AVCaptureMetadataOutput()
        captureSession.addOutput(captureMetadataOutput)

        captureMetadataOutput.setMetadataObjectsDelegate(
            objectsDelegate = this,
            queue = dispatch_get_main_queue(),
        )
        captureMetadataOutput.metadataObjectTypes = config.formats.toMetadataObjectTypes()

        previewLayer.apply {
            setVideoGravity(videoGravity = AVLayerVideoGravityResizeAspectFill)
            setSession(session = captureSession)
        }

        captureSession.startRunning()
    }

    /**
     * Resets the camera view to its initial state.
     *
     * Stops the capture session to reset camera state.
     * Useful for recovering from errors or changing camera settings.
     */
    internal actual fun onReset() {
        captureSession.stopRunning()
    }

    /**
     * Releases camera resources.
     *
     * Stops the capture session to properly clean up camera resources
     * and stop the preview.
     */
    internal actual fun onRelease() {
        captureSession.stopRunning()
    }

    /**
     * Changes state of camera flash to opposite of current.
     *
     * Provides control of flash in torch mode only.
     */
    internal actual fun toggleFlash() {
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
        if (device?.isTorchAvailable() == true) {
            val targetMode = when (device.torchMode) {
                AVCaptureTorchModeOn -> AVCaptureTorchModeOff
                AVCaptureTorchModeOff -> AVCaptureTorchModeOn
                else -> AVCaptureTorchModeAuto
            }
            device.setFlashMode(targetMode)
        }
    }

    /**
     * Check for flash is currently on in torch mode.
     */
    internal actual fun isFlashIsOn(): Boolean {
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo) ?: return false
        return device.isTorchAvailable() && device.torchMode == AVCaptureTorchModeOn
    }

    /**
     * Changes what camera is active at the moment.
     *
     * Provides control of what camera, frontal or back, is currently active.
     */
    internal actual fun toggleActiveCamera() {
        val frontCameraDeviceInput = AVCaptureDevice.defaultDeviceWithDeviceType(
            AVCaptureDeviceTypeBuiltInTripleCamera,
            AVMediaTypeVideo,
            AVCaptureDevicePositionFront
        ) as? AVCaptureDeviceInput
        val backCameraDeviceInput = AVCaptureDevice.defaultDeviceWithDeviceType(
            AVCaptureDeviceTypeBuiltInTripleCamera,
            AVMediaTypeVideo,
            AVCaptureDevicePositionBack
        ) as? AVCaptureDeviceInput

        if (frontCameraDeviceInput !=null && backCameraDeviceInput != null) {
            captureSession.beginConfiguration()
            if (captureSession.inputs.contains(frontCameraDeviceInput)) {
                captureSession.removeInput(frontCameraDeviceInput)
                captureSession.addInput(backCameraDeviceInput)
            } else if (captureSession.inputs.contains(backCameraDeviceInput)) {
                captureSession.removeInput(backCameraDeviceInput)
                captureSession.addInput(frontCameraDeviceInput)
            }
            captureSession.commitConfiguration()
        }
    }
}
