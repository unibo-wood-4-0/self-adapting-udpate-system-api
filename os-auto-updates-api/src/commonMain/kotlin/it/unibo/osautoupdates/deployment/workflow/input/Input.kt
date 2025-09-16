@file:UseSerializers(NonEmptyListSerializer::class)

package it.unibo.osautoupdates.deployment.workflow.input

import arrow.core.serialization.NonEmptyListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

/**
 * Input that may change the state of the software.
 * The input is used in a [Transition].
 */
@Serializable
@SerialName(Input.SERIAL_NAME)
sealed interface Input {
    /**
     * Utility for the [Input] class.
     */
    companion object {
        /**
         * The name of the serialization for the [Input].
         */
        const val SERIAL_NAME = "Input"
    }
}
