package it.unibo.osautoupdates.it.unibo.osautoupdates.resolution.software

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.software.ReleasedSoftware
import it.unibo.osautoupdates.software.ReleasedSoftware.Companion.releasedSoftware
import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.software.SoftwareExtensions.find
import it.unibo.osautoupdates.software.SoftwareExtensions.flatten
import it.unibo.osautoupdates.software.SoftwareExtensions.fold
import it.unibo.osautoupdates.software.version.Version.Companion.toVersion
import it.unibo.osautoupdates.software.version.span.SingleVersion
import it.unibo.osautoupdates.software.version.span.VersionSpan.Companion.single

class SoftwareExtensionsTest :
    ShouldSpec({

        fun singleVersionReleasedSw(
            name: String,
            lambda: ReleasedSoftware.Builder.() -> Unit = {
            },
        ): ReleasedSoftware =
            releasedSoftware(
                name,
                single("1.0.0".toVersion()),
                lambda,
            )

        val sw =
            singleVersionReleasedSw("test") {
                dependencies {
                    +singleVersionReleasedSw("dep1") {
                        dependencies {
                            +singleVersionReleasedSw("dep3")
                            +singleVersionReleasedSw("dep4")
                            +singleVersionReleasedSw("dep5")
                        }
                    }
                    +singleVersionReleasedSw("dep2")
                }
            }

        should("be able to find a specific dependency by name") {
            sw.find { it.name == "dep3" } shouldBe singleVersionReleasedSw("dep3")
        }
        should("be able to fail if no specific dependency by name could be found") {
            sw.find { it.name == "dep19" } shouldBe null
        }

        should("be able to count the total number software, including all the dependencies") {
            sw.fold(0) { acc, _ -> acc + 1 } shouldBe 6
        }
        should("be able to check that all the versions, including all dependencies, are single versions") {
            sw.fold(true) { acc, sw -> acc && sw.versionSpan is SingleVersion } shouldBe true
        }
        should("be able to check that all the versions, including all dependencies, are exactly 1.0.0") {
            sw.fold(true) { acc, sw -> acc && sw.versionSpan == single("1.0.0".toVersion()) } shouldBe true
        }
        should("be able to check if a specific dependency is present") {
            sw.fold(false) { acc, sw -> acc || sw.name == "dep3" } shouldBe true
        }

        should("be able to flatten a software with dependencies to a set of software with no dependencies") {
            val swToFlatten = ResolvedSoftware.Companion.resolvedSoftware("a", "1.0.0")

            val swB = ResolvedSoftware.Companion.resolvedSoftware("b", "2.0.0")
            val swD = ResolvedSoftware.Companion.resolvedSoftware("d", "4.0.0")
            val swC = ResolvedSoftware.Companion.resolvedSoftware("c", "3.0.0") { dependencies { +swD } }
            val swF = ResolvedSoftware.Companion.resolvedSoftware("f", "6.0.0")
            val swE = ResolvedSoftware.Companion.resolvedSoftware("e", "5.0.0") { dependencies { +swF } }
            val complexSwToFlatten =
                ResolvedSoftware.Companion.resolvedSoftware("a", "1.0.0") {
                    dependencies {
                        +swB
                        +swC
                        +swE
                    }
                }
            swToFlatten.flatten() shouldBe setOf(swToFlatten)
            complexSwToFlatten.flatten() shouldBe setOf(complexSwToFlatten, swB, swC, swD, swE, swF)
        }
    })
