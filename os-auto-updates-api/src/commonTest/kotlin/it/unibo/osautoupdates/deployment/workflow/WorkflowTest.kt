package it.unibo.osautoupdates.deployment.workflow

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.deployment.workflow.Transition.Companion.transition
import it.unibo.osautoupdates.deployment.workflow.Workflow.Companion.buildWorkflow
import it.unibo.osautoupdates.deployment.workflow.input.Fail
import it.unibo.osautoupdates.deployment.workflow.input.Success
import it.unibo.osautoupdates.deployment.workflow.state.Deployed
import it.unibo.osautoupdates.deployment.workflow.state.NotDeployed
import it.unibo.osautoupdates.failure.DeploymentFailure

class WorkflowTest :
    ShouldSpec({
        should("be empty when initialized without transitions") {
            val workflow = Workflow()
            workflow.transitions shouldBe emptyList()
        }

        should("add transitions using the builder, from empty and from clone") {
            val workflow =
                buildWorkflow {
                    +(transition(Success) toState Deployed)
                }
            workflow.transitions.size shouldBe 1
            checkFirstTransition(workflow)
            val failInput = Fail(DeploymentFailure.InstallFailure(""))
            val updatedWorkflow =
                buildWorkflow(workflow) {
                    +(transition(failInput) toState NotDeployed)
                }
            updatedWorkflow.transitions.size shouldBe 2
            checkFirstTransition(updatedWorkflow)
            val transition = updatedWorkflow.transitions.last()
            transition.input shouldBe failInput
            transition.targetState shouldBe NotDeployed
        }
    })

private fun checkFirstTransition(workflow: Workflow) {
    val transition = workflow.transitions.first()
    transition.input shouldBe Success
    transition.targetState shouldBe Deployed
}
