package it.unibo.osautoupdates.deployment.workflow

import it.unibo.osautoupdates.deployment.workflow.input.Input
import it.unibo.osautoupdates.deployment.workflow.state.State
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * A transition in a Finite State Machine.
 * @param input the input that will trigger a state change.
 * @param targetState the new state in which the FSM will be after the input is processed.
 */

@Serializable
data class Transition(
    val input: Input,
    val targetState: State,
) {
    /**
     * Utility for the [Transition] class.
     */
    companion object {
        /**
         * Used to specify the input that will trigger a state change.
         * @param input the input that will trigger a state change.
         */
        infix fun transition(input: Input): IncompleteTransition = IncompleteTransition(input)

        @JvmInline
        /**
         * Used to specify the target state of a transition.
         * @param input the input that will trigger a state change.
         * @return an [IncompleteTransition] that will be completed with the target state.
         */
        value class IncompleteTransition internal constructor(
            val input: Input,
        ) {
            /**
             * Specify the target state of the transition and create the [Transition].
             * @param targetState the new state.
             * @return the [Transition] that will be triggered by the input and will change the state to [targetState].
             */
            infix fun toState(targetState: State): Transition = Transition(input, targetState)
        }
    }
}
