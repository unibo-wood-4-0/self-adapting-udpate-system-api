
@file:UseSerializers(NonEmptyListSerializer::class)

package it.unibo.osautoupdates.deployment.suite

import arrow.core.Nel
import arrow.core.serialization.NonEmptyListSerializer
import it.unibo.osautoupdates.failure.Failure
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

/**
 * Result of the deployment.
 */
@Serializable
@SerialName("SuiteDeploymentState")
sealed interface SuiteDeploymentState {
    /**
     * The deployment has started, and it is still in progress.
     */
    @Serializable
    @SerialName("Started")
    data object Started : SuiteDeploymentState

    /**
     * The deployment has been deployed successfully, no failures occurred.
     */
    @Serializable
    @SerialName("Deployed")
    data object Deployed : SuiteDeploymentState

    /**
     * The deployment has failed, at least one failure occurred.
     * @param failures the list of failures that occurred during the deployment.
     */
    @Serializable
    @SerialName("Failed")
    data class Failed(
        val failures: Nel<Failure>,
    ) : SuiteDeploymentState

    /**
     * The deployment is outdated and can't be used anymore.
     * Usually, this [SuiteDeploymentState] is used when a downgrade or rollback is performed,
     * so that the SuiteDeployment cannot be used anymore.
     */
    @Serializable
    @SerialName("Outdated")
    data object Outdated : SuiteDeploymentState
}
