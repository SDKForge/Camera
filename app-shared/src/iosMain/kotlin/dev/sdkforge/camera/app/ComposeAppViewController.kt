package dev.sdkforge.camera.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import dev.sdkforge.camera.domain.CameraConfig
import dev.sdkforge.camera.domain.Facing
import dev.sdkforge.camera.domain.Format
import dev.sdkforge.camera.ui.rememberCameraController
import kotlin.experimental.ExperimentalObjCName
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.requestAccessForMediaType
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert

@OptIn(ExperimentalObjCName::class)
@Suppress("FunctionName", "unused")
@ObjCName("create")
fun ComposeAppViewController() = ComposeUIViewController(
    configure = {
        enforceStrictPlistSanityCheck = false
    },
) {
    val uiViewController = LocalUIViewController.current

    val cameraController = rememberCameraController(
        config = CameraConfig(
            formats = setOf(Format.QR_CODE),
            enableTorch = false,
            useHapticFeedback = true,
            cameraFacing = Facing.BACK,
        ),
    )

    App(
        cameraController = cameraController,
        modifier = Modifier
            .fillMaxSize(),
    )

    LaunchedEffect(Unit) {
        cameraController.scannedResults.collect { scanResult ->
            val alert = UIAlertController.alertControllerWithTitle(
                title = "Scanned format: ${scanResult.format}",
                message = "Scanned value: ${scanResult.value}",
                preferredStyle = UIAlertControllerStyleAlert,
            ).apply {
                addAction(
                    action = UIAlertAction.actionWithTitle(
                        title = "Ok",
                        style = UIAlertActionStyleDefault,
                        handler = null,
                    ),
                )
            }
            uiViewController.presentViewController(
                viewControllerToPresent = alert,
                animated = true,
                completion = null,
            )
        }
    }

    LaunchedEffect(cameraController.cameraState.hasCameraPermission) {
        if (!cameraController.cameraState.hasCameraPermission) {
            val alert = UIAlertController.alertControllerWithTitle(
                title = "Permission request",
                message = "Request access for video MediaType",
                preferredStyle = UIAlertControllerStyleAlert,
            ).apply {
                addAction(
                    action = UIAlertAction.actionWithTitle(
                        title = "Cancel",
                        style = UIAlertActionStyleCancel,
                        handler = { Unit },
                    ),
                )
                addAction(
                    action = UIAlertAction.actionWithTitle(
                        title = "Request",
                        style = UIAlertActionStyleDefault,
                        handler = {
                            /*
                            UIApplication.sharedApplication.openURL(
                                url = NSURL(string = UIApplicationOpenSettingsURLString),
                                options = emptyMap<Any?, Any>(),
                                completionHandler = null,
                            )
                             */
                            AVCaptureDevice.requestAccessForMediaType(
                                mediaType = AVMediaTypeVideo,
                                completionHandler = { isGranted ->
                                    println("Request access for video MediaType isGranted: $isGranted")
                                },
                            )
                        },
                    ),
                )
            }
            uiViewController.presentViewController(
                viewControllerToPresent = alert,
                animated = true,
                completion = null,
            )
        }
    }
}
