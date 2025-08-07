@file:Suppress("ktlint:standard:filename")

package dev.sdkforge.camera.core

import kotlin.test.Test
import kotlin.test.assertTrue

class IosPlatformTest {

    @Test
    fun testPlatformDefinition() {
        assertTrue(currentPlatform.name.contains("iOS", ignoreCase = true), "Check iOS is mentioned")
    }
}
