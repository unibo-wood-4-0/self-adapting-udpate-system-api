package it.unibo.osautoupdates.ds

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.system.oscommand.OsCommand
import it.unibo.osautoupdates.system.packageManager.WinGet

class PackageManagerDeploymentStrategyTest :
    ShouldSpec({

        val deployStrategy =
            PackageManagerDS(
                WinGet,
                preInstall = emptyList(),
                postInstall = listOf(OsCommand("a")),
            )
        should("assign the correct values to the properties") {
            deployStrategy.apply {
                packageManager shouldBe WinGet
                preInstall shouldBe emptyList()
                postInstall shouldBe listOf(OsCommand("a"))
            }
        }
    })
