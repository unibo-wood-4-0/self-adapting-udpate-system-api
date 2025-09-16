package it.unibo.osautoupdates.ds

import arrow.core.nel
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.ds.artifact.OperationSpecification
import it.unibo.osautoupdates.ds.artifact.Remote
import it.unibo.osautoupdates.system.oscommand.OsCommand

class ArtifactDSTest :
    ShouldSpec({

        val fetchSpec =
            Remote(
                uri = "http://www.losethegame.com/",
                fileName = "file.txt",
            )
        val artifactSpec =
            OperationSpecification(
                installCommands = OsCommand("installCommand").nel(),
                uninstallCommands = OsCommand("uninstallCommand").nel(),
            )
        val pre = OsCommand("preInstall").nel()
        val post = listOf(OsCommand("postInstall"), OsCommand("postInstall2"))

        val artifactDS =
            ArtifactDS(
                fetch = fetchSpec,
                operations = artifactSpec,
                preInstall = pre,
                postInstall = post,
            )

        should("assign the correct values to the properties") {
            with(artifactDS) {
                fetch shouldBe fetchSpec
                operations shouldBe artifactSpec
                preInstall shouldBe pre
                postInstall shouldBe post
                supportCache shouldBe true
            }
        }
    })
