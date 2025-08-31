@file:Suppress("ktlint:standard:class-signature")

package dev.sdkforge.camera.domain

/**
 * Result of a barcode scanning operation.
 *
 * This data class contains the information extracted from a scanned barcode,
 * including the format type and the decoded value.
 *
 * @property format The detected barcode format. May be [Format.UNKNOWN] if the format cannot be determined.
 * @property value The decoded string value from the barcode. May be null if decoding fails.
 */
data class ScanResult(
    /**
     * The detected barcode format.
     *
     * Indicates the specific barcode format that was scanned.
     * Will be [Format.UNKNOWN] if the scanner cannot determine the format.
     */
    val format: Format,

    /**
     * The decoded string value from the barcode.
     *
     * Contains the actual data encoded in the barcode.
     * May be null if the barcode could not be decoded properly.
     */
    val value: String?,
)
