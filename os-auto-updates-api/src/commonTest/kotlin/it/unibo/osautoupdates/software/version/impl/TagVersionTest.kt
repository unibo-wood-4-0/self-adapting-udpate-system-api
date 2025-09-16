package it.unibo.osautoupdates.software.version.impl

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.software.version.TagVersion
import it.unibo.osautoupdates.software.version.Version

class TagVersionTest :
    ShouldSpec({

        val tagVersion = Version.tag("latest")
        should("have the correct tag value") {
            when (tagVersion) {
                is TagVersion -> tagVersion.tag shouldBe "latest"
                else -> error("$tagVersion is not an instance of SemanticVersion")
            }
        }
        should("compare only to other TagVersion") {
            tagVersion.compareTo(tagVersion) shouldBe 0
            tagVersion.compareTo(Version.tag("latest")) shouldBe 0
            tagVersion.compareTo(Version.tag("v1.23.4")) shouldBe -1
            shouldThrow<IllegalStateException> { tagVersion.compareTo(Version.semantic(1, 2, 3)) }
        }
        should("return the tag in simplified format using toString") {
            tagVersion.toString() shouldBe "latest"
        }
        should("have a regex that matches formatted string correctly") {
            listOf(
                "latest",
                "v1.23.4",
                "ilGioco",
                "233425r223swvf",
            ).forEach {
                TagVersion.REGEX.matches(it) shouldBe true
            }
        }
        should("return the same tag as a string using toString") {
            tagVersion.toString() shouldBe "latest"
        }
    })
