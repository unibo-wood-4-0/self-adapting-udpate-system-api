package it.unibo.osautoupdates.workspace

import arrow.core.raise.Raise
import it.unibo.osautoupdates.configuration.Configuration
import it.unibo.osautoupdates.failure.IOFailure
import it.unibo.osautoupdates.failure.InitializationFailure
import it.unibo.osautoupdates.util.ArrowExtensions.withError
import it.unibo.osautoupdates.util.logs.LogFolder
import it.unibo.osautoupdates.workspace.ConfigurationInWorkspace.Companion.configurationInWorkspace
import it.unibo.osautoupdates.workspace.Workspace.Location
import kotlin.jvm.JvmInline
import kotlinx.io.files.Path

/**
 * A [Workspace] instance, representing the working space of the application when it needs to interact with the file
 * system.
 * The folder in which data is stored is platform-dependent, but it is always a folder that the application can write to.
 * @return a [Workspace]
 */
interface Workspace {
    /**
     * [Path] at which the workspace is located.
     * @param path the path at which the workspace is located.
     */
    @JvmInline
    value class Location(
        val path: Path,
    ) {
        constructor(string: String) : this(Path(string))
    }

    /**
     * The path at which the workspace is located.
     */
    val location: Location

    /**
     * The folder where logs are stored.
     */
    val logFolder: LogFolder

    context(_: Raise<InitializationFailure>)
    fun verify(): VerifiedWorkspace {
        val configurationInWorkspace =
            withError({ failure: IOFailure ->
                InitializationFailure.CheckFailure(failure.message)
            }) {
                location.configurationInWorkspace()
            }
        return VerifiedWorkspace(location, logFolder, configurationInWorkspace.configurationOrRaise())
    }
}

/**
 * Represents a verified workspace with a valid configuration.
 * This is the state of the workspace after it has been successfully verified and the configuration is loaded.
 */
data class VerifiedWorkspace(
    override val location: Location,
    override val logFolder: LogFolder,
    val configuration: Configuration,
) : Workspace
