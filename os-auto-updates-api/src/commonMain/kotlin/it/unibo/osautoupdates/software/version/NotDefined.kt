package it.unibo.osautoupdates.software.version

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A type of [Version] not specified.
 */

@Serializable
@SerialName(NotDefined.SERIAL_NAME)
data object NotDefined : Version {
    override fun compareTo(other: Version): Int = 0

    /**
     * @return the [NotDefined] as a string. Used for serialization.
     */
    const val SERIAL_NAME = "NotDefined"

    /**
     * @return the [NotDefined] Regex. It supports the following strings: "Undefined", "null", "nil", "none", "nothing".
     */

    val REGEX: Regex = Regex("[N-n]ot[D-d]efined|[U-u]ndefined|null|nil|none|nothing")
}
