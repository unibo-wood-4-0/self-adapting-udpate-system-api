package it.unibo.osautoupdates.system.oscommand

import arrow.core.NonEmptySet
import arrow.core.nonEmptySetOf
import arrow.core.raise.recover
import io.kotest.assertions.AssertionErrorBuilder.Companion.fail
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class OsCommandOutputTest :
    ShouldSpec({
        should("create a command output with code, stdOut and stdErr") {
            val commandOutput = OsCommandOutput(0, "stdOut", "stdErr", OsCommand.fromSingleString(""))
            commandOutput.code shouldBe 0
            commandOutput.stdOut shouldBe "stdOut"
            commandOutput.stdErr shouldBe "stdErr"
        }

        should("convert to logs correctly") {
            val successCases =
                listOf(
                    (1 to nonEmptySetOf(1, 2)),
                    (22 to nonEmptySetOf(22, 0)),
                    (0 to nonEmptySetOf(0)),
                    (1 to nonEmptySetOf(0, 3, 4, 5, 1)),
                )
            successCases.map { (exitCode, codes) ->
                recover({
                    commandWithSuccessfulCodes(exitCode, codes).outputIfSuccessful()
                }) {
                    fail("The exit code ($exitCode) is in the successful codes ($codes), should have succeded.")
                }
            }

            val failCases =
                listOf(
                    (1 to nonEmptySetOf(0)),
                    (22 to nonEmptySetOf(0, 1)),
                    (3 to nonEmptySetOf(0, 1, 2)),
                )
            failCases.map { (exitCode, codes) ->
                recover({
                    commandWithSuccessfulCodes(exitCode, codes).outputIfSuccessful()
                    fail("The exit code ($exitCode) is not in the successful codes ($codes), should have failed.")
                }) { }
            }
        }
    })

private fun commandWithSuccessfulCodes(
    exitCode: Int,
    codes: NonEmptySet<Int>,
): OsCommandOutput = OsCommandOutput(exitCode, "stdOut", "stdErr", OsCommand.fromSingleString("", codes))
