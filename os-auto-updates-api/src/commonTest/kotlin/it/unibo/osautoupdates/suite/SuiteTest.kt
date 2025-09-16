package it.unibo.osautoupdates.suite

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.ds.PackageManagerDS
import it.unibo.osautoupdates.software.ResolvedSoftware.Companion.resolvedSoftware
import it.unibo.osautoupdates.suite.Suite.Companion.resolvedSuite
import it.unibo.osautoupdates.system.packageManager.Dnf
import it.unibo.osautoupdates.system.packageManager.Pip
import it.unibo.osautoupdates.system.packageManager.WinGet

class SuiteTest :
    ShouldSpec({
        val resolvedSuite =
            resolvedSuite {
                +resolvedSoftware("sw", "1.0.0")
            }
        val aSoftware =
            resolvedSoftware("A", "1.0.0") {
                dss { +PackageManagerDS(Dnf) }
                dependencies {
                    +resolvedSoftware("B", "1.0.0") {
                        dss { PackageManagerDS(Pip) }
                    }
                    +resolvedSoftware("C", "1.0.0")
                }
            }
        val dSoftware =
            resolvedSoftware("D", "1.0.0") {
                dss { +PackageManagerDS(Pip) }
            }
        val eSoftware =
            resolvedSoftware("E", "1.0.0") {
                dss { +PackageManagerDS(WinGet) }
            }
        val complexSuite =
            resolvedSuite {
                +aSoftware
                +dSoftware
                +eSoftware
            }
        should("obtain the PackageManager required by the suite") {
            complexSuite.requiredPackageManagers() shouldBe setOf(Dnf, Pip, WinGet)
        }

        should("retrieve a software by its name") {
            listOf(
                "A" to aSoftware,
                "D" to dSoftware,
                "E" to eSoftware,
                "NOT_PRESENT" to null,
            ).forEach { (softwareName, expectedSoftware) ->
                complexSuite.software(softwareName) shouldBe expectedSoftware
            }
        }
    })
