package it.unibo.osautoupdates.system.os

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.serialization.Formatters

class OsTest :
    ShouldSpec({

        val windows = Os(Windows, Edition("Windows 10"))
        val ubuntu = Os(Linux, Edition("Ubuntu 20.04"))

        should("be serializable excluding the value classes") {
            val encodedWindows = Formatters.json().encodeToString(Os.serializer(), windows)
            encodedWindows shouldBe """{"family":"Windows","edition":"Windows 10"}"""
        }
        should("create an instance with Linux family and a specific edition") {
            ubuntu.family shouldBe Linux
            ubuntu.edition shouldBe Edition("Ubuntu 20.04")
        }

        should("create an instance with Windows family and a specific edition") {
            windows.family shouldBe Windows
            windows.edition shouldBe Edition("Windows 10")
        }

        should("return a list of supported operating systems") {
            val supported = Family.supported()
            supported shouldBe listOf(Linux, Windows)
        }
    })
