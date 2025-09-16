package it.unibo.osautoupdates.software.version.impl

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.software.version.SemanticVersion
import it.unibo.osautoupdates.software.version.Version
import it.unibo.osautoupdates.software.version.Version.Companion.semantic

class SemanticVersionTest :
    ShouldSpec({

        val semanticVersion = semantic(1, 2, 3)

        should("have the correct major, minor, and patch values") {
            when (semanticVersion) {
                is SemanticVersion -> {
                    semanticVersion.major shouldBe 1
                    semanticVersion.minor shouldBe 2
                    semanticVersion.patch shouldBe 3
                }
                else -> error("$semanticVersion is not an instance of SemanticVersion")
            }
        }

        should("throw IllegalArgumentException when assigning values below 0") {
            listOf(
                { semantic(-1, 2, 3) },
                { semantic(-1, 2, 3) },
                { semantic(-1, -2, 3) },
                { semantic(-1, -2, -3) },
                { semantic(1, -2, 3) },
                { semantic(1, -2, -3) },
                { semantic(1, 2, -3) },
                { semantic(-1, 2, -3) },
                { semantic(-1, -2, -3) },
            ).forEach {
                shouldThrow<IllegalArgumentException> { it.invoke() }
            }
        }

        should("compare correctly to other Versions") {
            semantic(1, 0, 0).compareTo(semantic(1, 0, 0)) shouldBe 0
            semantic(1, 0, 1).compareTo(semantic(1, 0, 1)) shouldBe 0
            semantic(0, 67, 1).compareTo(semantic(0, 67, 1)) shouldBe 0
            semantic(1, 0, 0).compareTo(semantic(2, 0, 0)) shouldBeLessThan 0
            semantic(1, 0, 0).compareTo(semantic(1, 1, 0)) shouldBeLessThan 0
            semantic(1, 0, 0).compareTo(semantic(1, 0, 1)) shouldBeLessThan 0
            semantic(2, 0, 0).compareTo(semantic(1, 0, 0)) shouldBeGreaterThan 0
            semantic(1, 1, 0).compareTo(semantic(1, 0, 0)) shouldBeGreaterThan 0
            semantic(0, 0, 40).compareTo(semantic(0, 0, 39)) shouldBeGreaterThan 0
        }

        should("return the version in simplified format using toString") {
            semanticVersion.toString() shouldBe "1.2.3"
        }

        should("have a regex that matches formatted string correctly") {
            listOf(
                "1.2.30",
                "0.1.0",
                "2.0.0",
                "v0.23.0",
                "v123.31.41",
            ).forEach {
                SemanticVersion.REGEX.matches(it) shouldBe true
            }
            listOf(
                "1.2.30.1004",
                "1.0.0.0067",
                "1.4",
                "a1.4.3",
            ).forEach {
                SemanticVersion.REGEX.matches(it) shouldBe false
            }
        }
    })
