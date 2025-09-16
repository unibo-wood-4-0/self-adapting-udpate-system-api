@file:OptIn(ExperimentalJsExport::class)

package it.unibo.osautoupdates.ds.failureStrategy

import it.unibo.osautoupdates.failure.DeploymentFailure
import kotlin.js.ExperimentalJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [FailureStrategy] to simulate for this [FailDS].
 */
@Serializable
sealed interface FailureStrategy {
    /**
     * Convert this [FailureStrategy] to a [DeploymentFailure].
     */

    fun toDeploymentError(): DeploymentFailure =
        when (this) {
            is PreInstallFailureStrategy -> DeploymentFailure.PreInstallFailure("")
            is UninstallFailureStrategy -> DeploymentFailure.UninstallFailure("")
            is InstallFailureStrategy -> DeploymentFailure.InstallFailure("")
            is FetchFailureStrategy -> DeploymentFailure.FetchFailure("")
            is PostInstallFailureStrategy -> DeploymentFailure.PostInstallFailure("")
        }

    /**
     * Utility for the [FailureStrategy] class.
     */
    companion object {
        /**
         * @return a [List] of all the [FailureStrategy]s that may occur during the deployment.
         * [UninstallFailureStrategy] is not included because it is not used in the deployment process,
         * but only in the rollback and downgrade processes.
         */
        fun allDeploymentFailures(): List<FailureStrategy> =
            listOf(
                PreInstallFailureStrategy,
                InstallFailureStrategy,
                FetchFailureStrategy,
                PostInstallFailureStrategy,
            )
    }
}

/**
 * The [FailureStrategy] to simulate before the installation.
 */
@Serializable
@SerialName("PreInstallErrorStrategy")
data object PreInstallFailureStrategy : FailureStrategy

/**
 * The [FailureStrategy] to simulate after the installation.
 */
@Serializable
@SerialName("PostInstallErrorStrategy")
data object PostInstallFailureStrategy : FailureStrategy

/**
 * The [FailureStrategy] to simulate when fetching the package.
 */
@Serializable
@SerialName("FetchErrorStrategy")
data object FetchFailureStrategy : FailureStrategy

/**
 * The [FailureStrategy] to simulate when installing the package.
 */
@Serializable
@SerialName("InstallErrorStrategy")
data object InstallFailureStrategy : FailureStrategy

/**
 * The [FailureStrategy] to simulate when uninstalling the package.
 */
@Serializable
@SerialName("UninstallErrorStrategy")
data object UninstallFailureStrategy : FailureStrategy
