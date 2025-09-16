package it.unibo.osautoupdates.deployment.suite

import it.unibo.osautoupdates.deployment.software.SoftwareDeployment
import it.unibo.osautoupdates.suite.ResolvedSuite
import it.unibo.osautoupdates.util.StringId

/**
 * Extension functions for the [SuiteDeployment] class.
 * Set a new id for the [SuiteDeployment].
 */
fun SuiteDeployment.withId(id: StringId): SuiteDeployment = this.copy(id = id)

/**
 * Extension functions for the [SuiteDeployment] class.
 * Set a new [ResolvedSuite] for the [SuiteDeployment].
 */
fun SuiteDeployment.withSuite(resolvedSuite: ResolvedSuite): SuiteDeployment = this.copy(resolvedSuite = resolvedSuite)

/**
 * Extension functions for the [SuiteDeployment] class.
 * Set a new [Map] of [SoftwareDeployment] for the [SuiteDeployment].
 */
fun SuiteDeployment.withSoftwareDeployments(softwareDeployments: List<SoftwareDeployment>): SuiteDeployment =
    this.copy(softwareDeployments = softwareDeployments)

/**
 * Extension functions for the [SuiteDeployment] class.
 * Add a new entry to the [softwareDeployments] map.
 * @param swDeployment The [SoftwareDeployment] to add.
 * @return a new [SuiteDeployment] with the new entry.
 */
fun SuiteDeployment.addOrReplaceSoftwareDeployment(swDeployment: SoftwareDeployment): SuiteDeployment =
    this.copy(softwareDeployments = this.softwareDeployments.filter { it.name != swDeployment.name } + swDeployment)

/**
 * Extension functions for the [SuiteDeployment] class.
 * Set a new [SuiteDeploymentState] for the [SuiteDeployment].
 */
fun SuiteDeployment.withState(state: SuiteDeploymentState): SuiteDeployment = this.copy(state = state)
