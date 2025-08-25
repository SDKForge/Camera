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
import platform.AVFoundation.AVCaptureDevicePositionBack
import platform.AVFoundation.AVCaptureDevicePositionFront
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInTripleCamera
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
import platform.AVFoundation.isTorchModeSupported
import platform.AVFoundation.torchMode
import platform.CoreGraphics.CGRectZero
import platform.QuartzCore.CALayer
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
internal actual class PlatformCameraView(
    private val config: CameraConfig,
    private val onScan: (result: ScanResult) -> Unit,
) : NSObject(),
    AVCaptureMetadataOutputObjectsDelegateProtocol {

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

    internal val captureSession: AVCaptureSession = AVCaptureSession()

    internal val previewLayer: AVCaptureVideoPreviewLayer = AVCaptureVideoPreviewLayer().apply {
        uiView.layer.addSublayer(this)
    }

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

    internal actual fun onPermissionGranted() {
        onInit()
    }

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

    internal actual fun onReset() {
        captureSession.stopRunning()
    }

    internal actual fun onRelease() {
        captureSession.stopRunning()
    }
}
