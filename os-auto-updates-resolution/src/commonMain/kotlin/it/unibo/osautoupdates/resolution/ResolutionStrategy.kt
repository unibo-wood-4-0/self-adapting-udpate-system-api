package it.unibo.osautoupdates.resolution

import it.unibo.osautoupdates.serialization.common.StringSurrogateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

/**
 * A [ResolutionStrategy] typealias.
 */
typealias RS = ResolutionStrategy

/**
 * Formal description of how a software chain of dependencies will be resolved before its deployment.
 * In fact, a piece of software can support a wide range of dependencies' versions to properly work.
 * This component has the responsibility to elect the [it.unibo.osautoupdates.software.version.merger.span.SingleVersion] that is going to be deployed.
 * The invoke method maps a [it.unibo.osautoupdates.software.version.merger.span.RangeVersion] to a [it.unibo.osautoupdates.software.version.merger.span.SingleVersion].
 * This is the most important step of the [ResolutionStrategy].
 */

@Serializable
@SerialName(ResolutionStrategy.SERIAL_NAME)
sealed interface ResolutionStrategy {
    /**
     * Utility for the [ResolutionStrategy] class.
     */
    companion object {
        /**
         * The name of the [ResolutionStrategy] type, used for serialization.
         */
        const val SERIAL_NAME = "ResolutionStrategy"
    }
}

/**
 * Serialization of [ResolutionStrategy] to and from a string using the serial name.
 */
object ResolutionStrategyStringSerializer : StringSurrogateSerializer<RS>(
    "ResolutionStrategy",
    {
        when (it) {
            MaxRS.SERIAL_NAME -> MaxRS
            PreferredRS.SERIAL_NAME -> PreferredRS
            else -> throw SerializationException("Unknown ResolutionStrategy: $it")
        }
    },
)
