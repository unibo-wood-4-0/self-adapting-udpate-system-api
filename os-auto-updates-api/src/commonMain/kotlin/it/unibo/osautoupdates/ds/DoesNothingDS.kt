package it.unibo.osautoupdates.ds

import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A [DeploymentStrategy] that does nothing and always succeeds.
 */
@Serializable
@SerialName(DoesNothingDS.SERIAL_NAME)
data object DoesNothingDS : DS {
    override val supportCache: Boolean get() = false

    override val preInstall: List<OsCommand> = emptyList()

    override val postInstall: List<OsCommand> = emptyList()

    /**
     * The name of the [DoesNothingDS] type, used for serialization.
     */
    const val SERIAL_NAME = "DoesNothing"
}
