package it.unibo.osautoupdates.resolution

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A [ResolutionStrategy] that always chooses the preferred possible version.
 */
@Serializable
@SerialName(PreferredRS.SERIAL_NAME)
data object PreferredRS : ResolutionStrategy {
    /**
     * The name of the [PreferredRS] type, used for serialization.
     */
    const val SERIAL_NAME = "Preferred"

    /**
     * @return a string representation of the object.
     */
    override fun toString(): String = SERIAL_NAME
}
