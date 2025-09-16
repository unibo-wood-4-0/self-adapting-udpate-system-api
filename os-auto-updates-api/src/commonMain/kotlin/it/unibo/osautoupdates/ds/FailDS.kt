package it.unibo.osautoupdates.ds

import it.unibo.osautoupdates.ds.failureStrategy.FailureStrategy
import it.unibo.osautoupdates.ds.failureStrategy.PostInstallFailureStrategy
import it.unibo.osautoupdates.ds.failureStrategy.PreInstallFailureStrategy
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [DeploymentStrategy] that fails based on the given [FailureStrategy].
 * @param failureStrategy the [FailureStrategy] to use.
 */
@Serializable
@SerialName(FailDS.SERIAL_NAME)
data class FailDS(
    val failureStrategy: FailureStrategy,
) : DeploymentStrategy {
    override val supportCache: Boolean get() = false

    override val preInstall: List<OsCommand> = commandsOrEmpty(PreInstallFailureStrategy)

    override val postInstall: List<OsCommand> = commandsOrEmpty(PostInstallFailureStrategy)

    private fun commandsOrEmpty(strategy: FailureStrategy?) =
        if (failureStrategy == strategy) {
            listOf(OsCommand("exit", "1"))
        } else {
            emptyList()
        }

    /**
     * Utility for the [FailDS] class.
     */
    companion object {
        /**
         * The name of the [FailDS] type, used for serialization.
         */
        const val SERIAL_NAME = "Testing"
    }
}
