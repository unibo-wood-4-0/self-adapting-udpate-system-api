package it.unibo.osautoupdates.system.packageManager

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.software.ResolvedSoftware.Companion.resolvedSoftware
import it.unibo.osautoupdates.software.version.NotDefined
import it.unibo.osautoupdates.system.oscommand.OsCommand

class PackageManagerTest :
    ShouldSpec({

        should("base command should be toolbox run") {
            Toolbox(Dnf).baseCommand shouldBe OsCommand("toolbox", "run")
        }
        should("check that the whole methods of toolbox get overridden by the decorator pattern") {
            val sw = resolvedSoftware("A", NotDefined) { }
            Toolbox(Dnf).installCommand(sw) shouldBe
                OsCommand("toolbox", "run", "sudo", "dnf", "-y", "install", "A")
            Toolbox(Dnf).uninstallCommand(sw) shouldBe
                OsCommand("toolbox", "run", "sudo", "dnf", "-y", "remove", "A")
        }
    })
