package dev.sdkforge.camera.ui

import com.google.mlkit.vision.barcode.common.Barcode as MLBarcode

internal val Format.value: Int
    get() = when (this) {
        Format.AZTEC -> MLBarcode.FORMAT_AZTEC
        Format.CODE_39 -> MLBarcode.FORMAT_CODE_39
        Format.CODE_93 -> MLBarcode.FORMAT_CODE_93
        Format.CODE_128 -> MLBarcode.FORMAT_CODE_128
        Format.DATA_MATRIX -> MLBarcode.FORMAT_DATA_MATRIX
        Format.EAN_8 -> MLBarcode.FORMAT_EAN_8
        Format.EAN_13 -> MLBarcode.FORMAT_EAN_13
        Format.ITF -> MLBarcode.FORMAT_ITF
        Format.PDF417 -> MLBarcode.FORMAT_PDF417
        Format.QR_CODE -> MLBarcode.FORMAT_QR_CODE
        Format.UPC_E -> MLBarcode.FORMAT_UPC_E
        Format.UNKNOWN -> MLBarcode.FORMAT_UNKNOWN
    }

internal val Int.asFormat: Format
    get() = when (this) {
        MLBarcode.FORMAT_AZTEC -> Format.AZTEC
        MLBarcode.FORMAT_CODE_39 -> Format.CODE_39
        MLBarcode.FORMAT_CODE_93 -> Format.CODE_93
        MLBarcode.FORMAT_CODE_128 -> Format.CODE_128
        MLBarcode.FORMAT_DATA_MATRIX -> Format.DATA_MATRIX
        MLBarcode.FORMAT_EAN_8 -> Format.EAN_8
        MLBarcode.FORMAT_EAN_13 -> Format.EAN_13
        MLBarcode.FORMAT_ITF -> Format.ITF
        MLBarcode.FORMAT_PDF417 -> Format.PDF417
        MLBarcode.FORMAT_QR_CODE -> Format.QR_CODE
        MLBarcode.FORMAT_UPC_E -> Format.UPC_E
        else -> Format.UNKNOWN
    }

internal fun Collection<Format>.toBarcodeScannerOptions() = mapNotNull { if (it == Format.UNKNOWN) null else it.value }
    .reduceOrNull { f1, f2 -> f1 or f2 }
