package it.unibo.osautoupdates.ds

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.ds.DeploymentStrategy
import it.unibo.osautoupdates.ds.FailDS
import it.unibo.osautoupdates.ds.failureStrategy.PreInstallFailureStrategy

class FailureDSTest :
    ShouldSpec({
        val failDSDSWithError =
            FailDS(
                failureStrategy = PreInstallFailureStrategy,
            )

        should("assign the correct values to the properties") {
            failDSDSWithError.failureStrategy shouldBe PreInstallFailureStrategy
        }
    })
