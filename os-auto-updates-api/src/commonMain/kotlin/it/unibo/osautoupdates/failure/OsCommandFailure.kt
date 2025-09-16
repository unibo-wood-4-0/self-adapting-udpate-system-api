package it.unibo.osautoupdates.failure

import it.unibo.osautoupdates.system.oscommand.OsCommand
import it.unibo.osautoupdates.system.oscommand.OsCommandOutput
import kotlinx.serialization.Serializable

/**
 * [SoftFailure] that can happen during the execution of an [OsCommand].
 */

@Serializable
sealed interface OsCommandFailure : SoftFailure {
    /**
     * [OsCommandFailure] that indicates that the command threw an unexpected low-level exception that could not be
     * handled.
     * @param command the command that was executed.
     * @param exceptionName the name of the exception that was thrown.
     * @param exceptionMessage the message of the exception that was thrown.
     */
    @Serializable
    class Runtime(
        val command: OsCommand,
        private val exceptionName: String = "",
        private val exceptionMessage: String = "",
    ) : OsCommandFailure {
        /**
         * Constructor that takes an [OsCommand] and an [Exception].
         * @param command the command that was executed.
         * @param exception the exception that was thrown.
         */
        constructor(command: OsCommand, exception: Exception) : this(
            command,
            exception::class.simpleName ?: "Exception",
            exception.message ?: "No message was provided.",
        )

        override val message: String =
            """
            |Command threw an unexpected low-level exception that could not be handled.
            |Command: $command
            |Exception: $exceptionName - $exceptionMessage
            """.trimMargin()
    }

    /**
     * [OsCommandFailure] that indicates that the command exited with an unsuccessful code.
     * @param output the output of the command.
     * @property code the exit code of the command.
     */
    @Serializable
    data class Unsuccessful(
        val output: OsCommandOutput,
    ) : OsCommandFailure {
        override val message: String = output.describe()
    }

    /**
     * [OsCommandFailure] that indicates that no command was specified to execute.
     */
    @Serializable
    data object NoCommandSpecified : OsCommandFailure {
        override val message: String = "No commands were specified, there was nothing to do."
    }
}
