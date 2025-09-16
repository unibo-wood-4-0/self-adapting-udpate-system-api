package it.unibo.osautoupdates.deployment.workflow

import it.unibo.osautoupdates.deployment.workflow.input.Fail
import it.unibo.osautoupdates.deployment.workflow.state.Deployed
import it.unibo.osautoupdates.deployment.workflow.state.NotDeployed
import it.unibo.osautoupdates.deployment.workflow.state.State
import it.unibo.osautoupdates.deployment.workflow.state.Uninstall
import it.unibo.osautoupdates.failure.SoftFailure
import it.unibo.osautoupdates.validation.ValidationResult
import kotlinx.serialization.Serializable

/**
 * The ordered list of [Transition]s that describes the deployment process of a software.
 * @param transitions The list of [Transition]s that describes the deployment process of a software.
 */

@Serializable
data class Workflow(
    val transitions: List<Transition> = emptyList(),
) {
    /**
     * [Builder] for [Workflow].
     * @param startingWorkflow the [Workflow] to start from.
     */
    class Builder(
        startingWorkflow: Workflow = Workflow(),
    ) {
        private val transitions: MutableList<Transition> = startingWorkflow.transitions.toMutableList()

        /**
         * Add a new [Transition] to the [Workflow].
         * @param transition the [Transition] to add.
         */
        operator fun Transition.unaryPlus() {
            transitions.add(this)
        }

        /**
         * Build the [Workflow] with the added [Transition]s.
         */
        fun build(): Workflow = Workflow(transitions)
    }

    /**
     * The failures occurred during the deployment process.
     */
    fun failures(): List<SoftFailure> =
        transitions
            .map { it.input }
            .filterIsInstance<Fail>()
            .flatMap { it.failures }

    /**
     * The progression in the deployment process of a software.
     */
    fun finalState(): State? = transitions.lastOrNull()?.targetState

    /**
     * Check if the software is deployed by checking if the final state is [Deployed].
     */
    fun isDeployed(): Boolean = finalState() is Deployed

    /**
     * Check if the software is not deployed by checking if the final state is [NotDeployed].
     */
    fun isNotDeployed(): Boolean = finalState() is NotDeployed

    /**
     * Check if the timeline include a failed event at any point.
     */
    fun isFailed(): Boolean = transitions.any { it.input is Fail }

    /**
     * @return true if the software was uninstalled. In order for this method to return true, the software must:
     * - have been deployed at least once before (contains the [Deployed] state)
     * - have been uninstalled (contains the [Uninstall] Input)
     * - the final state must be [NotDeployed]
     */
    fun hasBeenUninstalled(): Boolean =
        containsState(Deployed) && finalState() is NotDeployed && containsState(Uninstall)

    private fun containsState(state: State): Boolean = transitions.any { it.targetState == state }

    /**
     * @return the [ValidationResult] if it is present in the [FSM].
     */
    fun validationResult(): ValidationResult? =
        transitions
            .map { it.input }
            .filterIsInstance<ValidationResult>()
            .firstOrNull()

    /**
     * Utility for the [Workflow] class.
     */
    companion object {
        /**
         * Build a [Workflow] using the provided lambda.
         * @param lambda the lambda instructing the [Builder] on how to build the [Workflow].
         * @return the [Workflow] built.
         */
        suspend fun buildWorkflow(lambda: suspend Builder.() -> Unit): Workflow = buildWorkflow(Workflow(), lambda)

        /**
         * Build a [Workflow] using the provided lambda.
         * @param startingWorkflow the [Workflow] to start from.
         * @param lambda the lambda instructing the [Builder] on how to build the [Workflow].
         */
        suspend fun buildWorkflow(
            startingWorkflow: Workflow,
            lambda: suspend Builder.() -> Unit,
        ): Workflow = Builder(startingWorkflow).apply { lambda() }.build()
    }
}
