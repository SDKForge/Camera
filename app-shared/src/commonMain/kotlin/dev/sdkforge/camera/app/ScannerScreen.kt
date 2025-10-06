package dev.sdkforge.camera.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.sdkforge.camera.domain.ScanResult
import dev.sdkforge.camera.ui.CameraController
import dev.sdkforge.camera.ui.CameraView
import kotlinx.coroutines.launch

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
        ModeSwitcher(
            modifier = modifier.padding(top = innerPadding.calculateTopPadding()),
        )
//        ButtonsOverlay(
//            controller = cameraController,
//            modifier = modifier.padding(top = innerPadding.calculateTopPadding()),
//            onHistoryClicked = { isHistoryDialogShown = !isHistoryDialogShown },
//        )
//        ScansHistoryDialog(
//            showDialog = isHistoryDialogShown,
//            scans = scans,
//            onDismiss = { isHistoryDialogShown = false },
//        )
    }
}

@Composable
fun ModeSwitcher(
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val state = rememberLazyListState()
    val listOfItems = remember { mutableStateListOf(MenuItem("Camera"), MenuItem("Scanner")) }
    val cardBackgroundColor = remember { mutableStateOf(Color.Transparent) }

    val onClickEvent: (Int) -> Unit = {
        listOfItems.forEachIndexed { index, item ->
            item.isSelected = index == it
            cardBackgroundColor.value = if (item.isSelected) Color.White else Color.Transparent
            println("MY_LOG --- index: $index -- item: $item")
            scope.launch {
                state.animateScrollToItem(index)
            }
        }
    }
    LazyRow(
        state = state,
        modifier = modifier
            .padding(8.dp).fillMaxWidth(),
    ) {
        itemsIndexed(listOfItems) { index, item ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (item.isSelected) {
                        Color.White
                    } else {
                        Color.Transparent
                    },
                ),
                shape = RoundedCornerShape(
                    size = 24.dp,
                ),
                onClick = {
                    onClickEvent.invoke(index)
                },
            ) {
                Text(
                    text = item.name,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                )
            }
        }
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

data class MenuItem(val name: String, var isSelected: Boolean = false)
