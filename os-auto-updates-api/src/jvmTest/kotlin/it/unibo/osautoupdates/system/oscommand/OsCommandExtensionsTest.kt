package it.unibo.osautoupdates.system.oscommand

import arrow.core.raise.either
import arrow.core.raise.withError
import io.kotest.assertions.AssertionErrorBuilder.Companion.fail
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.failure.OsCommandFailure
import it.unibo.osautoupdates.system.os.OsCommandExtensions.invoke

class OsCommandExtensionsTest :
    WordSpec({
        "OsCommand" should {
            "correctly execute commands and return the output" {
                val exit0 = OsCommand.fromSingleString("exit 0")
                val exit1 = OsCommand.fromSingleString("exit 1")
                either {
                    withError({ e: OsCommandFailure -> fail("Command failed with error: $e") }) {
                        val output0 = exit0.invoke()
                        val output1 = exit1.invoke()
                        output0.code shouldBe 0
                        output1.code shouldBe 1
                    }
                }
            }
        }
    })
