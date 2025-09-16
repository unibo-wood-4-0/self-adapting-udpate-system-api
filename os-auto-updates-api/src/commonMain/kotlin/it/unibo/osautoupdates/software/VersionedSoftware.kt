package it.unibo.osautoupdates.software

import it.unibo.osautoupdates.software.VersionedSoftware.Companion.SERIAL_NAME
import it.unibo.osautoupdates.software.version.Version
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A piece of software that has a single version.
 */
@Serializable
@SerialName(SERIAL_NAME)
sealed class VersionedSoftware : Software {
    /**
     * @return the version of the [Software].
     */
    abstract val version: Version

    /**
     * @return a [SoftwareSpecification] with the name and the version of the [VersionedSoftware].
     */
    fun specification(): SoftwareSpecification = SoftwareSpecification(name, version)

    /**
     * Utility for the [VersionedSoftware] class.
     */
    companion object {
        /**
         * The name of the [VersionedSoftware] type, used for serialization.
         */
        const val SERIAL_NAME = "VersionedSoftware"
    }
}
