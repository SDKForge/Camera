package dev.sdkforge.camera.ui

import dev.sdkforge.camera.domain.Format
import com.google.mlkit.vision.barcode.common.Barcode as MLBarcode

/**
 * Extension property to get the ML Kit barcode format value.
 *
 * Maps our domain [Format] enum to the corresponding ML Kit barcode format
 * integer values used by the ML Kit barcode scanning API.
 *
 * @return The ML Kit barcode format integer value
 */
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

/**
 * Extension property to convert ML Kit barcode format to domain format.
 *
 * Maps ML Kit barcode format integer values back to our domain [Format] enum.
 * Unknown formats are mapped to [Format.UNKNOWN].
 *
 * @return The corresponding domain [Format] enum value
 */
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

/**
 * Extension function to convert format collection to ML Kit scanner options.
 *
 * Converts a collection of [Format] values to a bitwise OR combination
 * of ML Kit barcode format values suitable for the barcode scanner options.
 * Filters out [Format.UNKNOWN] values and returns null if no valid formats are provided.
 *
 * @return A bitwise OR combination of ML Kit format values, or null if no valid formats
 */
internal fun Collection<Format>.toBarcodeScannerOptions() = mapNotNull { if (it == Format.UNKNOWN) null else it.value }
    .reduceOrNull { f1, f2 -> f1 or f2 }
