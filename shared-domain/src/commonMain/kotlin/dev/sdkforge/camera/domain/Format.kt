package dev.sdkforge.camera.domain

/**
 * Supported barcode formats for scanning.
 *
 * This enum defines all the barcode and QR code formats that can be
 * detected and decoded by the camera scanner. Each format has specific
 * characteristics and use cases.
 */
enum class Format {
    /**
     * Aztec Code - 2D barcode format.
     *
     * High-density 2D barcode that can encode large amounts of data.
     * Commonly used in transportation and logistics applications.
     */
    AZTEC,

    /**
     * Code 39 - 1D barcode format.
     *
     * Widely used in industrial and logistics applications.
     * Can encode alphanumeric characters and some special symbols.
     */
    CODE_39,

    /**
     * Code 93 - 1D barcode format.
     *
     * Compact alternative to Code 39 with better error detection.
     * Used in logistics and inventory management.
     */
    CODE_93,

    /**
     * Code 128 - 1D barcode format.
     *
     * High-density barcode that can encode all ASCII characters.
     * Widely used in shipping and logistics.
     */
    CODE_128,

    /**
     * Data Matrix - 2D barcode format.
     *
     * Compact 2D barcode that can encode large amounts of data.
     * Used in electronics, healthcare, and logistics.
     */
    DATA_MATRIX,

    /**
     * EAN-8 - 1D barcode format.
     *
     * European Article Number with 8 digits.
     * Used for small retail products.
     */
    EAN_8,

    /**
     * EAN-13 - 1D barcode format.
     *
     * European Article Number with 13 digits.
     * Standard format for retail products worldwide.
     */
    EAN_13,

    /**
     * ITF (Interleaved 2 of 5) - 1D barcode format.
     *
     * Numeric-only barcode used in logistics and warehousing.
     * Compact format for encoding numbers.
     */
    ITF,

    /**
     * PDF417 - 2D barcode format.
     *
     * High-capacity 2D barcode used in transportation and identification.
     * Can encode large amounts of data including binary.
     */
    PDF417,

    /**
     * QR Code - 2D barcode format.
     *
     * Most popular 2D barcode format with high data capacity.
     * Widely used in marketing, payments, and general applications.
     */
    QR_CODE,

    /**
     * UPC-E - 1D barcode format.
     *
     * Compressed version of UPC-A with 6 digits.
     * Used for small retail products.
     */
    UPC_E,

    /**
     * Unknown or unrecognized barcode format.
     *
     * Used when the scanner detects a barcode but cannot
     * determine its specific format.
     */
    UNKNOWN,
}
