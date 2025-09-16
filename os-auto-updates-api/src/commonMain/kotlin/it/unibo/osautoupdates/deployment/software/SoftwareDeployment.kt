@file:OptIn(ExperimentalSerializationApi::class)

package it.unibo.osautoupdates.deployment.software

import it.unibo.osautoupdates.deployment.workflow.Workflow
import it.unibo.osautoupdates.deployment.workflow.input.CheckAlreadyInstalled
import it.unibo.osautoupdates.failure.Failure
import it.unibo.osautoupdates.software.SoftwareName
import kotlin.collections.orEmpty
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The result of a deployment process.
 * Include the failed and successful [Workflow]s.
 * @param name The name of the software.
 * @param workflows The [Workflow]s that are deployed or not.
 */

@Serializable
@SerialName(SoftwareDeployment.SERIAL_NAME)
data class SoftwareDeployment(
    val name: SoftwareName,
    val workflows: List<Workflow> = listOf(),
) {
    /**
     * The [Workflow] that is deployed, if any.
     */
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val deployed: Workflow? get() = workflows.find { it.isDeployed() }

    /**
     * The [Workflow]s that are not deployed.
     */
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val notDeployed: List<Workflow> = workflows.filter { it.isNotDeployed() }

    /**
     * Check if the [SoftwareDeployment] is deployed.
     * @return `true` if the [SoftwareDeployment] is deployed, `false` otherwise.
     */
    fun isDeployed(): Boolean = deployed != null

    /**
     * Check if the [SoftwareDeployment] is already deployed.
     * @return `true` if the [SoftwareDeployment] is already deployed, meaning that it needs to be deployed and have
     * a [CheckAlreadyInstalled.IsInstalled.SameVersion] input at some point in its deployed workflow,
     */
    fun isAlreadyDeployed(): Boolean =
        deployed?.transitions?.any { t ->
            t.input is CheckAlreadyInstalled.IsInstalled.SameVersion
        } ?: false

    /**
     * Check if the [SoftwareDeployment] is not deployed.
     * @return `true` if the [SoftwareDeployment] is not deployed, `false` otherwise.
     */
    fun isNotDeployed(): Boolean = !isDeployed()

    /**
     * Check if the [SoftwareDeployment] is failed.
     * @return `true` if the [SoftwareDeployment] is failed, `false` otherwise.
     */
    fun isFailed(): Boolean = failures().isNotEmpty()

    /**
     * @return a [List] of [Failure]s collected from all the workflows, including the [deployed] one.
     */
    fun failures(): List<Failure> = notDeployed.flatMap { it.failures() } + deployed?.failures().orEmpty()

    /**
     * Check if the software has been uninstalled and it is not deployed.
     * @return `true` if the software has been uninstalled, and it is not deployed, `false` otherwise.
     */
    fun hasBeenUninstalled(): Boolean = workflows.lastOrNull()?.hasBeenUninstalled() ?: false

    /**
     * Add a new [Workflow] to the [SoftwareDeployment].
     * If the [Workflow] is deployed, it will be set as the successful [Workflow].
     * Otherwise, it will be added to the failed [Workflow]s.
     * @param workflow the [Workflow] to add.
     * @return a new [SoftwareDeployment] with the added [Workflow].
     */
    operator fun plus(workflow: Workflow): SoftwareDeployment =
        SoftwareDeployment(
            name = name,
            workflows = workflows + workflow,
        )

    /**
     * Utility for the [SoftwareDeployment] class.
     */
    companion object {
        /**
         * The name of the serialization for the [SoftwareDeployment].
         */
        const val SERIAL_NAME = "SoftwareDeployment"
    }
}
