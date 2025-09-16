@file:OptIn(ExperimentalJsExport::class)

package it.unibo.osautoupdates.system.oscommand

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.core.raise.withError
import it.unibo.osautoupdates.failure.OsCommandFailure
import it.unibo.osautoupdates.util.ArrowExtensions.ensure
import it.unibo.osautoupdates.util.ArrowExtensions.withError
import kotlin.js.ExperimentalJsExport
import kotlinx.serialization.Serializable

/**
 * TypeAlias representing the exit code of a command.
 */
typealias ExitCode = Int

/**
 * TypeAlias representing the Standard output of a command.
 */
typealias StandardOutput = String?

/**
 * TypeAlias representing the Standard error of a command.
 */
typealias StandardError = String?

/**
 * Representation of a [OsCommand] output.
 * @property code the exit code of the command.
 * @property stdOut the standard output of the command.
 * @property stdErr the standard error of the command.
 * @property command the command that was executed.
 */
@Serializable
data class OsCommandOutput(
    val code: ExitCode,
    val stdOut: StandardOutput,
    val stdErr: StandardError,
    val command: OsCommand,
) {
    /**
     * @return a string describing the command output and how it was considered based on the [SuccessfulStrategy].
     */
    fun describe(): String =
        buildString {
            append("The command `${command.asSingleString()}` resulted in code ($code). ")
            append(command.successfulStrategy.description())
            if (stdOut?.isBlank() == false) {
                append("\nStandardOutput:")
                append(stdOut)
            }
            if (stdErr?.isBlank() == false) {
                append("\nStandardError:\n")
                append(stdErr)
            }
        }

    /**
     * @return whether the command was successful or not based on the [SuccessfulStrategy] of the [command].
     */
    fun isSuccessful(): Boolean = command.successfulStrategy.test()

    /**
     * Opposite of [isSuccessful].
     * @see [isSuccessful]
     */
    fun isNotSuccessful(): Boolean = !isSuccessful()

    /**
     * Retrieve the [OsCommandOutput] if the command was successful.
     * @receiver raise a [OsCommandFailure.Unsuccessful] if the command failed (returning a non-zero exit code).
     * @return this if the command was successful, or an [OsCommandFailure.Unsuccessful] if it failed.
     */
    context(_: Raise<OsCommandFailure.Unsuccessful>)
    fun outputIfSuccessful(): OsCommandOutput {
        ensure(isSuccessful()) {
            OsCommandFailure.Unsuccessful(this)
        }
        return this
    }

    /**
     * Converts the [OsCommandOutput] to [Logs] containing the [stdOut] or an error.
     * @param mapError the error to return if the command failed.
     * @receiver raise a [E] if the command failed (returning a non-zero exit code).
     * @return this if the command was successful, or an [E] if it failed.
     */
    context(_: Raise<E>)
    fun <E> outputIfSuccessful(mapError: (String) -> E): OsCommandOutput =
        withError({ unsuccessfulError -> mapError(unsuccessfulError.message) }) {
            outputIfSuccessful()
        }
}
