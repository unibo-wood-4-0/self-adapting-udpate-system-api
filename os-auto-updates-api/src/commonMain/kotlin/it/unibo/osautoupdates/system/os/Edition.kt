package it.unibo.osautoupdates.system.os

import it.unibo.osautoupdates.serialization.common.StringSurrogateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Edition of the operating system.
 * Basically a plain string that better describes the operating system.
 * @param edition The edition of the operating system.
 */

@Serializable(with = EditionSerializer::class)
@SerialName(Edition.SERIAL_NAME)
data class Edition(
    private val edition: String,
) {
    override fun toString(): String = edition

    /**
     * Utility for [Edition] class.
     */
    companion object {
        /**
         * @return the serial name of the [Edition] type, used for serialization.
         */
        const val SERIAL_NAME = "Edition"
    }
}

internal data object EditionSerializer : StringSurrogateSerializer<Edition>(
    "Edition",
    { Edition(it) },
)
