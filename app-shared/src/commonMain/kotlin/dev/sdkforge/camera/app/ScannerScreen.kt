package dev.sdkforge.camera.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.sdkforge.camera.domain.ScanResult
import dev.sdkforge.camera.ui.CameraController
import dev.sdkforge.camera.ui.CameraView

@Composable
fun ScannerScreen(
    cameraController: CameraController,
    scans: Set<ScanResult>,
    modifier: Modifier = Modifier,
) {
    var isHistoryDialogShown by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        CameraView(
            cameraController = cameraController,
            modifier = Modifier.fillMaxSize(),
        )
        ButtonsOverlay(
            controller = cameraController,
            modifier = modifier.padding(top = innerPadding.calculateTopPadding()),
            onHistoryClicked = { isHistoryDialogShown = !isHistoryDialogShown },
        )
        ScansHistoryDialog(
            showDialog = isHistoryDialogShown,
            scans = scans,
            onDismiss = { isHistoryDialogShown = false },
        )
    }
}

@Composable
fun ButtonsOverlay(
    controller: CameraController,
    modifier: Modifier = Modifier,
    onHistoryClicked: () -> Unit,
) {
    var isFlashOn by remember { mutableStateOf(false) }
    val targetIcon = if (isFlashOn) Icons.Default.FlashOff else Icons.Default.FlashOn

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top,
            modifier = modifier.fillMaxWidth(),
        ) {
            IconButton(
                onClick = {
                    onHistoryClicked.invoke()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Show scans history",
                    modifier = Modifier.clickable {
                        onHistoryClicked.invoke()
                    },
                )
            }
            IconButton(
                onClick = {
                    controller.toggleFlash()
                    isFlashOn = controller.isFlashIsOn()
                },
            ) {
                Icon(
                    imageVector = targetIcon,
                    contentDescription = "Flash toggle",
                )
            }
            IconButton(
                onClick = {
                    controller.toggleActiveCamera()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.FlipCameraAndroid,
                    contentDescription = "Flip between front and back active cameras",
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ScansHistoryDialog(
    showDialog: Boolean,
    scans: Set<ScanResult>,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    if (showDialog) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismiss.invoke()
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxSize(),
        ) {
            if (scans.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Scans history is empty",
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(8.dp).fillMaxWidth(),
                ) {
                    itemsIndexed(items = scans.toList()) { index, item ->
                        Text(
                            text = "${index + 1}. ${item.value}",
                            modifier = Modifier.padding(top = 12.dp),
                        )
                    }
                }
            }
        }
    }
}
