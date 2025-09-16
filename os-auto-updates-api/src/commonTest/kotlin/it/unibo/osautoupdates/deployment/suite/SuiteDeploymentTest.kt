package it.unibo.osautoupdates.deployment.suite

import arrow.core.nel
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.deployment.software.SoftwareDeployment
import it.unibo.osautoupdates.deployment.workflow.Transition
import it.unibo.osautoupdates.deployment.workflow.Workflow
import it.unibo.osautoupdates.deployment.workflow.input.Success
import it.unibo.osautoupdates.deployment.workflow.state.Install
import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.suite.Suite
import it.unibo.osautoupdates.util.StringId

class SuiteDeploymentTest :
    ShouldSpec({

        should("have the correct field, making the Extension methods work") {

            val empty = SuiteDeployment.emptyDeployment()

            val softwareDeployment =
                listOf(
                    SoftwareDeployment(
                        "test",
                        Workflow
                            .buildWorkflow {
                                +(Transition.transition(Success) toState (Install))
                            }.nel(),
                    ),
                )

            val id = StringId("id")

            val state = SuiteDeploymentState.Started

            val suite =
                Suite.resolvedSuite {
                    +ResolvedSoftware.resolvedSoftware("test")
                }

            val suiteDeployment =
                empty
                    .withId(id)
                    .withState(state)
                    .withSuite(suite)
                    .withSoftwareDeployments(softwareDeployment)

            suiteDeployment.id shouldBe id
            suiteDeployment.state shouldBe state
            suiteDeployment.resolvedSuite shouldBe suite
            suiteDeployment.softwareDeployments shouldBe softwareDeployment
            suiteDeployment.softwareDeployment("test") shouldBe softwareDeployment.first()
            suiteDeployment.softwareDeployment("notPresent") shouldBe null
        }
    })
