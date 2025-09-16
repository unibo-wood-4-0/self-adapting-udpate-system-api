package it.unibo.osautoupdates.software.version.span

import it.unibo.osautoupdates.software.version.Version
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A [VersionSpan] that contains a single [it.unibo.osautoupdates.software.version.Version].
 * @param version the [it.unibo.osautoupdates.software.version.Version] contained in the [VersionSpan].
 */
@Serializable
@SerialName(SingleVersion.SERIAL_NAME)
data class SingleVersion(
    val version: Version,
) : VersionSpan {
    /**
     * Utility for the [SingleVersion] class.
     */
    companion object {
        /**
         * The name of the [SingleVersion] type, used for serialization.
         */
        const val SERIAL_NAME = "Single"
    }
}
