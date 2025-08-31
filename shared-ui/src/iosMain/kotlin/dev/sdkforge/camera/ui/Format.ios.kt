package dev.sdkforge.camera.ui

import dev.sdkforge.camera.domain.Format
import platform.AVFoundation.AVMetadataObjectTypeAztecCode
import platform.AVFoundation.AVMetadataObjectTypeCode128Code
import platform.AVFoundation.AVMetadataObjectTypeCode39Code
import platform.AVFoundation.AVMetadataObjectTypeCode93Code
import platform.AVFoundation.AVMetadataObjectTypeDataMatrixCode
import platform.AVFoundation.AVMetadataObjectTypeEAN13Code
import platform.AVFoundation.AVMetadataObjectTypeEAN8Code
import platform.AVFoundation.AVMetadataObjectTypeITF14Code
import platform.AVFoundation.AVMetadataObjectTypePDF417Code
import platform.AVFoundation.AVMetadataObjectTypeQRCode
import platform.AVFoundation.AVMetadataObjectTypeUPCECode

/**
 * Extension property to get the AVFoundation metadata object type.
 *
 * Maps our domain [Format] enum to the corresponding AVFoundation
 * metadata object type strings used by the iOS barcode scanning API.
 *
 * @return The AVFoundation metadata object type string, or null for unknown formats
 */
internal val Format.value: String?
    get() = when (this) {
        Format.AZTEC -> AVMetadataObjectTypeAztecCode
        Format.CODE_39 -> AVMetadataObjectTypeCode39Code
        Format.CODE_93 -> AVMetadataObjectTypeCode93Code
        Format.CODE_128 -> AVMetadataObjectTypeCode128Code
        Format.DATA_MATRIX -> AVMetadataObjectTypeDataMatrixCode
        Format.EAN_8 -> AVMetadataObjectTypeEAN8Code
        Format.EAN_13 -> AVMetadataObjectTypeEAN13Code
        Format.ITF -> AVMetadataObjectTypeITF14Code
        Format.PDF417 -> AVMetadataObjectTypePDF417Code
        Format.QR_CODE -> AVMetadataObjectTypeQRCode
        Format.UPC_E -> AVMetadataObjectTypeUPCECode
        else -> null
    }

/**
 * Extension property to convert AVFoundation metadata object type to domain format.
 *
 * Maps AVFoundation metadata object type strings back to our domain [Format] enum.
 * Unknown formats are mapped to [Format.UNKNOWN].
 *
 * @return The corresponding domain [Format] enum value
 */
internal val String?.asFormat: Format
    get() = when (this) {
        AVMetadataObjectTypeAztecCode -> Format.AZTEC
        AVMetadataObjectTypeCode39Code -> Format.CODE_39
        AVMetadataObjectTypeCode93Code -> Format.CODE_93
        AVMetadataObjectTypeCode128Code -> Format.CODE_128
        AVMetadataObjectTypeDataMatrixCode -> Format.DATA_MATRIX
        AVMetadataObjectTypeEAN8Code -> Format.EAN_8
        AVMetadataObjectTypeEAN13Code -> Format.EAN_13
        AVMetadataObjectTypeITF14Code -> Format.ITF
        AVMetadataObjectTypePDF417Code -> Format.PDF417
        AVMetadataObjectTypeQRCode -> Format.QR_CODE
        AVMetadataObjectTypeUPCECode -> Format.UPC_E
        else -> Format.UNKNOWN
    }

/**
 * Extension function to convert format collection to metadata object types.
 *
 * Converts a collection of [Format] values to a list of AVFoundation
 * metadata object type strings suitable for the metadata output configuration.
 * Filters out [Format.UNKNOWN] values and null results.
 *
 * @return A list of AVFoundation metadata object type strings
 */
internal fun Collection<Format>.toMetadataObjectTypes() = mapNotNull { if (it == Format.UNKNOWN) null else it.value }
