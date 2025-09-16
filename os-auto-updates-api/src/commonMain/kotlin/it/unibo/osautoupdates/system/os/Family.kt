@file:OptIn(ExperimentalJsExport::class)

package it.unibo.osautoupdates.system.os

import arrow.core.raise.Raise
import it.unibo.osautoupdates.failure.IOFailure
import it.unibo.osautoupdates.serialization.common.StringSurrogateSerializer
import it.unibo.osautoupdates.util.ArrowExtensions.raise
import kotlin.js.ExperimentalJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

/**
 * Operating System family. Include the operating systems supported by the library.
 */
@Serializable(with = FamilyAsStringSerializer::class)
@SerialName(Family.SERIAL_NAME)
sealed interface Family {
    /**
     * Utility for the [Family] class.
     */
    companion object {
        context(_: Raise<IOFailure>)
        fun fromString(family: String): Family =
            when (family) {
                "Windows" -> Windows
                "Linux" -> Linux
                else ->
                    raise(
                        IOFailure.UnprocessableContent(
                            "Unknown operating system family: $family," +
                                "only ${supported()} are supported.",
                        ),
                    )
            }

        /**
         * @return a list of all supported [Family]
         */
        fun supported(): List<Family> = listOf(Linux, Windows)

        /**
         * The name of the [Family] type, used for serialization.
         */
        const val SERIAL_NAME = "Family"
    }
}

/**
 * Linux operating system.
 */
@Serializable
@SerialName(Linux.SERIAL_NAME)
data object Linux : Family {
    override fun toString(): String = SERIAL_NAME

    const val SERIAL_NAME = "Linux"
}

/**
 * Windows operating system.
 */
@Serializable
@SerialName(Windows.SERIAL_NAME)
data object Windows : Family {
    override fun toString(): String = SERIAL_NAME

    const val SERIAL_NAME = "Windows"
}

internal data object FamilyAsStringSerializer : StringSurrogateSerializer<Family>(
    Family.SERIAL_NAME,
    { string ->
        when (string) {
            "Linux" -> Linux
            "Windows" -> Windows
            else -> throw SerializationException(
                "Unknown operating system family: $string, only ${Family.supported()} are supported.",
            )
        }
    },
)
