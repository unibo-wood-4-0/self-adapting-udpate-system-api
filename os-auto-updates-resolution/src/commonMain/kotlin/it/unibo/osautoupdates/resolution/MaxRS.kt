package it.unibo.osautoupdates.resolution

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A [ResolutionStrategy] that always chooses the greatest possible version.
 */
@Serializable
@SerialName(MaxRS.SERIAL_NAME)
data object MaxRS : ResolutionStrategy {
    /**
     * The name of the [MaxRS] type, used for serialization.
     */
    const val SERIAL_NAME = "Max"

    /**
     * @return a string representation of the object.
     */
    override fun toString(): String = SERIAL_NAME
}
