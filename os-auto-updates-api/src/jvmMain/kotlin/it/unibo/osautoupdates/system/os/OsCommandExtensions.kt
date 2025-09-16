

package it.unibo.osautoupdates.system.os

import arrow.core.raise.Raise
import arrow.core.raise.catch
import arrow.core.raise.ensure
import it.unibo.osautoupdates.failure.OsCommandFailure
import it.unibo.osautoupdates.system.oscommand.OsCommand
import it.unibo.osautoupdates.system.oscommand.OsCommandOutput
import it.unibo.osautoupdates.util.ArrowExtensions.ensure
import it.unibo.osautoupdates.util.ArrowExtensions.raise
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors

/**
 * Extensions for [OsCommand] and related classes.
 */
object OsCommandExtensions {
    /**
     * Replace the commands of the [OsCommand] with the given list of commands, keeping everything else as is.
     * @receiver the [OsCommand] to modify.
     * @param commands the new list of commands.
     */
    fun OsCommand.withCommands(commands: List<String>): OsCommand =
        OsCommand(
            commands,
            this.successfulStrategy,
        )

    /**
     * Execute the given [OsCommand].
     * @receiver the [OsCommand] to execute.
     * @receiver raise a [OsCommandFailure] if the command fails:
     *  - [OsCommandFailure.NoCommandSpecified] if no command is specified.
     * @return the corresponding [OsCommandOutput].
     */
    context(_: Raise<OsCommandFailure>)
    operator fun OsCommand.invoke(): OsCommandOutput {
        ensure(commands.isNotEmpty() || !commands.all { it.isBlank() }) {
            OsCommandFailure.NoCommandSpecified
        }
        return catch({
            val processBuilder = ProcessBuilder(commands.forCurrentPlatform())
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val content = reader.lines().collect(Collectors.joining("\n"))
            OsCommandOutput(
                process.waitFor(),
                content,
                process.errorStream.toText,
                this,
            )
        }) { exception: Exception ->
            // Catch any low-level exception and wrap it in the error.
            raise(OsCommandFailure.Runtime(this, exception))
        }
    }

    private fun List<String>.forCurrentPlatform() =
        when (Os.currentFamily()) {
            is Windows -> listOf("cmd", "/C") + this
            is Linux -> listOf("sh", "-c", this.joinToString(" "))
        }

    private val InputStream.toText: String get() = bufferedReader().use(BufferedReader::readText)
}
