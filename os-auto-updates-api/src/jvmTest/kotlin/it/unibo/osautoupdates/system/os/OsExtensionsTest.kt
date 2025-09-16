package it.unibo.osautoupdates.system.os

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeIn

class OsExtensionsTest :
    ShouldSpec({
        should("return the current operating system family") {
            val currentFamily = Os.currentFamily()
            // This test assumes the current OS is Linux or Windows
            currentFamily.shouldBeIn(Linux, Windows)
        }
    })
