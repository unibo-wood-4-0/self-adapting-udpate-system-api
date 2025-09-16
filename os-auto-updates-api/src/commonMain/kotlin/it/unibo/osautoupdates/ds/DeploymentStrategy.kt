package it.unibo.osautoupdates.ds

import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Typealias for [DeploymentStrategy].
 */
typealias DS = DeploymentStrategy

/**
 * Formal description of how a single piece of software needs to be deployed on a machine.
 * For example: the tools to be used, the specific set of commands needed, etc.
 */

@Serializable
@SerialName(DeploymentStrategy.SERIAL_NAME)
sealed interface DeploymentStrategy {
    /**
     * A list of consecutive pre-installation [OsCommand] in the form of [String] to be executed before the installation
     * of the software.
     */
    val preInstall: List<OsCommand>

    /**
     * A list of consecutive post-installation [OsCommand] in the form of [String] to be executed after the installation
     * of the software.
     */
    val postInstall: List<OsCommand>

    /**
     * Whether the deployment strategy supports caching, saving the fetched resources for later use.
     */
    val supportCache: Boolean

    /**
     * Utility for the [DeploymentStrategy] class.
     */
    companion object {
        /**
         * The name of the [DeploymentStrategy] type, used for serialization.
         */
        const val SERIAL_NAME = "DeploymentStrategy"

        /**
         * Supported [DeploymentStrategy] names, using the Serial name.
         */
        fun supported(): Set<String> = setOf(ArtifactDS.SERIAL_NAME, PackageManagerDS.SERIAL_NAME)
    }
}
