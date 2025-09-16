package it.unibo.osautoupdates.system.packageManager

import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [Toolbox] [PackageManager] used for system packages.
 * It is a wrapper for [PackageManager]s in different environments.
 * The decorator pattern is used to add the "toolbox run" command to the base command of the [PackageManager].
 */
@Serializable
@SerialName(Toolbox.SERIAL_NAME)
data class Toolbox(
    private val packageManager: PackageManager,
) : PackageManager {
    override val baseCommand: OsCommand = OsCommand("toolbox", "run")

    override fun installCommand(software: ResolvedSoftware): OsCommand {
        val pmInstallCommand = packageManager.installCommand(software)
        return OsCommand(
            commands = baseCommand.commands + pmInstallCommand.commands,
            successfulStrategy = pmInstallCommand.successfulStrategy,
        )
    }

    override fun uninstallCommand(software: ResolvedSoftware): OsCommand {
        val pmUninstallCommand = packageManager.uninstallCommand(software)
        return OsCommand(
            commands = baseCommand.commands + pmUninstallCommand.commands,
            successfulStrategy = pmUninstallCommand.successfulStrategy,
        )
    }

    /**
     * Utility for the [Toolbox] class.
     */
    companion object {
        /**
         * The name of the [PackageManager] type, used for serialization.
         */
        internal const val SERIAL_NAME = "Toolbox"
    }
}
