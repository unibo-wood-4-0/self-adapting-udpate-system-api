package it.unibo.osautoupdates.deployment.workflow.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A possible state of a [FSM].
 */
@Serializable
@SerialName(State.SERIAL_NAME)
sealed interface State {
    /**
     * The final state of the software deployment process.
     */
    @Serializable
    sealed interface Final : State

    /**
     * Utility for the [State] class.
     */
    companion object {
        /**
         * The name of the serialization for the [State].
         */
        const val SERIAL_NAME = "State"
    }
}
