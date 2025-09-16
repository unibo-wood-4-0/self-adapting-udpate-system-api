package it.unibo.osautoupdates.software

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.software.version.NotDefined
import it.unibo.osautoupdates.software.version.Version.Companion.semantic

class SoftwareSpecificationVersionTest :
    ShouldSpec({

        val semantic = SoftwareSpecification("TestSoftware", semantic(1, 0, 0))
        val customSeparator = SoftwareSpecification("CustomSoftware", semantic(2, 1, 3))
        val notDefined = SoftwareSpecification("UndefinedVersionSoftware", NotDefined)

        should("return a string with default separator and defined version") {
            semantic.asString() shouldBe "TestSoftware-1.0.0"
        }
        should("return a string with custom separator and defined version") {
            customSeparator.asString("_") shouldBe "CustomSoftware_2.1.3"
        }
        should("return a string without version when the version is NotDefined") {
            notDefined.asString() shouldBe "UndefinedVersionSoftware"
        }
    })
