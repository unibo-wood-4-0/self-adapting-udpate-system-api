package it.unibo.osautoupdates.workspace

import arrow.core.raise.Raise
import arrow.core.raise.catch
import it.unibo.osautoupdates.configuration.Configuration
import it.unibo.osautoupdates.failure.IOFailure
import it.unibo.osautoupdates.failure.InitializationFailure
import it.unibo.osautoupdates.serialization.Formatters
import it.unibo.osautoupdates.system.fs.FileExtensions.toFile
import it.unibo.osautoupdates.util.ArrowExtensions.ensure
import it.unibo.osautoupdates.util.ArrowExtensions.raise
import java.io.File
import kotlinx.io.files.Path

/**
 * Interface that represents the configuration contained in a [Workspace].
 */
interface ConfigurationInWorkspace {
    /**
     * @return the [Configuration] of the workspace if present, otherwise raises a
     * [InitializationFailure.MissingConfiguration].
     */
    context(_: Raise<InitializationFailure.MissingConfiguration>)
    fun configurationOrRaise(): Configuration

    /**
     * @return the [Configuration] of the workspace if present, otherwise returns null.
     */
    val configuration: Configuration?

    companion object {
        context(_: Raise<IOFailure>)
        fun Workspace.Location.assignNewConfiguration(newConfiguration: Configuration) =
            assignConfigurationToLocation(this, newConfiguration)

        context(_: Raise<IOFailure>)
        fun Workspace.Location.configurationInWorkspace(): ConfigurationInWorkspace =
            buildConfigurationInWorkspace(this)
    }
}

private class ConfigurationInWorkspaceImpl(
    private val location: Workspace.Location,
) : ConfigurationInWorkspace {
    private val configurationPath: Path
        get() = Path("${location.path}/${CONFIGURATION_FILE_NAME}")
    private val configurationFile: File = configurationPath.toFile()

    override var configuration: Configuration? = null
        private set

    /**
     * Attempts to retrieve the configuration from the configuration file at [configurationPath].
     * This method will fail if the configuration file does not exist or is not a valid YAML file.
     */
    context(_: Raise<IOFailure>)
    fun deserializeConfigurationFile(): Configuration {
        ensure(configurationFile.exists() && configurationFile.isFile) {
            IOFailure.NotFound(configurationFile.absolutePath)
        }
        return catch({
            Formatters.yaml().decodeFromString(
                Configuration.serializer(),
                configurationFile.readText(Charsets.UTF_8),
            )
        }) { exception ->
            raise(
                IOFailure.UnprocessableContent(
                    "Failed to deserialize configuration from file: $configurationPath. " +
                        "An Exception occurred - ${exception.javaClass.simpleName}: ${exception.message}",
                ),
            )
        }
    }

    context(_: Raise<InitializationFailure.MissingConfiguration>)
    override fun configurationOrRaise(): Configuration =
        configuration ?: raise(InitializationFailure.MissingConfiguration)

    /**
     * Loads configuration if a valid file exists.
     */
    context(_: Raise<IOFailure>)
    fun loadConfigurationIfExists() {
        if (configurationFile.exists() && configurationFile.isFile) {
            configuration = deserializeConfigurationFile()
        }
    }
}

context(_: Raise<IOFailure>)
private fun assignConfigurationToLocation(
    location: Workspace.Location,
    newConfiguration: Configuration,
) {
    val configPath = Path("${location.path}/$CONFIGURATION_FILE_NAME").toFile()
    catch({
        val yamlString =
            Formatters.yaml().encodeToString(
                Configuration.serializer(),
                newConfiguration,
            )
        configPath.writeText(yamlString)
    }) { exception ->
        raise(
            IOFailure.FileSystemInteraction(
                "Failed to write new configuration to file: $configPath. " +
                    "An Exception occurred - ${exception.javaClass.simpleName}: ${exception.message}",
            ),
        )
    }
}

context(_: Raise<IOFailure>)
private fun buildConfigurationInWorkspace(location: Workspace.Location): ConfigurationInWorkspace =
    ConfigurationInWorkspaceImpl(location).apply {
        loadConfigurationIfExists()
    }

private const val CONFIGURATION_FILE_NAME = "configuration.yaml"
