package it.unibo.osautoupdates.system.packageManager

import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.software.version.NotDefined
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private val logger = KotlinLogging.logger {}

/**
 * A [PackageManager] is a system component that is able to install, uninstall and list software that are currently
 * installed on the system. It is also able to search for software that are available to be installed.
 */
@Serializable
@SerialName(PackageManager.SERIAL_NAME)
sealed interface PackageManager {
    /**
     * The [OsCommand] used by the [PackageManager] to perform its operations on the system.
     */
    val baseCommand: OsCommand

    /**
     * Installs the given [ResolvedSoftware] on the system.
     * @param software the [ResolvedSoftware] to install.
     * @return a new [OsCommand] that contains the command to install the given [ResolvedSoftware].
     */

    fun installCommand(software: ResolvedSoftware): OsCommand

    /**
     * Uninstalls the given [ResolvedSoftware] from the system.
     * @param software the [ResolvedSoftware] to uninstall.
     * @return a new [OsCommand] that contains the command to uninstall the given [ResolvedSoftware].
     */

    fun uninstallCommand(software: ResolvedSoftware): OsCommand

    /**
     * utility for the [PackageManager] class.
     */
    companion object {
        /**
         * The name of the [PackageManager] type, used for serialization.
         */
        internal const val SERIAL_NAME = "PackageManager"

        /**
         * Logs a warning message for the specified [ResolvedSoftware] that contains a version.
         * Useful when a [PackageManager] does not support version specification.
         * @param software the [ResolvedSoftware] that contains a version.
         * @param pmName the name of the [PackageManager] that does not support version specification.
         */

        fun warningForSpecifiedVersions(
            software: ResolvedSoftware,
            pmName: String,
        ) {
            if (software.version != NotDefined) {
                logger.warn {
                    """
                    $pmName does not support version specification,
                    however ${software.name} provided ${software.version} as version.
                    The version will be ignored this time,
                    but the suite specification creation process is probably wrong and should be fixed.
                    """.trimIndent()
                }
            }
        }
    }
}
