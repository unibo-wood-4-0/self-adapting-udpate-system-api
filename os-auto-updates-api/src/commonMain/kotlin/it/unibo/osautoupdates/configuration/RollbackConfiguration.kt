package it.unibo.osautoupdates.configuration

import it.unibo.osautoupdates.cleanup.Cleanup
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The configuration for the rollback process.
 * @param enabled if true, the system will execute the rollback process upon a failed deployment.
 * @param cleanup the [Cleanup] configuration for the rollback process.
 */
@Serializable
@SerialName(RollbackConfiguration.SERIAL_NAME)
data class RollbackConfiguration(
    val enabled: Boolean,
    val cleanup: Cleanup,
) {
    companion object {
        const val SERIAL_NAME = "RollbackConfiguration"
    }
}
