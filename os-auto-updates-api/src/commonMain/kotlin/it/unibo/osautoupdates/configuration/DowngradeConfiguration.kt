package it.unibo.osautoupdates.configuration

import it.unibo.osautoupdates.cleanup.Cleanup
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The configuration for the downgrade process.
 * @param cleanup the [Cleanup] configuration for the downgrade process.
 */
@Serializable
@SerialName(DowngradeConfiguration.SERIAL_NAME)
data class DowngradeConfiguration(
    val cleanup: Cleanup,
) {
    companion object {
        const val SERIAL_NAME = "DowngradeConfiguration"
    }
}
