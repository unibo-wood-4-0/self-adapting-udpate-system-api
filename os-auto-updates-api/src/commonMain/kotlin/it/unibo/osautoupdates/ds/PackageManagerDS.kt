package it.unibo.osautoupdates.ds

import it.unibo.osautoupdates.system.oscommand.OsCommand
import it.unibo.osautoupdates.system.packageManager.PackageManager
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A [DeploymentStrategy] that uses a [PackageManager] to install a software.
 * @param packageManager the [PackageManager] to use.
 */
@Serializable
@SerialName(PackageManagerDS.SERIAL_NAME)
data class PackageManagerDS(
    val packageManager: PackageManager,
    override val preInstall: List<OsCommand> = emptyList(),
    override val postInstall: List<OsCommand> = emptyList(),
) : DeploymentStrategy {
    override val supportCache: Boolean get() = false

    companion object {
        /**
         * The name of the [PackageManagerDS] type, used for serialization.
         */
        const val SERIAL_NAME = "PackageManager"
    }
}
