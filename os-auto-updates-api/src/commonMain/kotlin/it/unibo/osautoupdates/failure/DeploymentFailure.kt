@file:UseSerializers(NonEmptyListSerializer::class)

package it.unibo.osautoupdates.failure

import arrow.core.serialization.NonEmptyListSerializer
import it.unibo.osautoupdates.software.ResolvedSoftware
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

/**
 * [it.unibo.osautoupdates.failure.SoftFailure] that can happen during the deployment step.
 */
@Serializable
@SerialName("DeploymentFailure")
sealed interface DeploymentFailure : SoftFailure {
    /**
     * [DeploymentFailure] that can happen during the fetch of the resources needed to deploy a [Software].
     */
    @Serializable
    @SerialName("FetchFailure")
    data class FetchFailure(
        override val message: String,
    ) : DeploymentFailure

    /**
     * [DeploymentFailure] that happens when a [ResolvedSoftware] can't be installed because some validation tests
     * didn't pass.
     * @param software the [ResolvedSoftware] that failed the validation tests.
     * @param failedValidations the list of failed validation tests.
     */
    @Serializable
    @SerialName("ValidationFailure")
    data class ValidationFailure(
        private val software: ResolvedSoftware,
        val failedValidations: List<String>,
    ) : DeploymentFailure {
        override val message: String =
            "`${software.name}` did not pass one or more validation tests." +
                "Validation tests are necessary to ensure the software is working correctly. " +
                "Processes that failed are: `$failedValidations`"
    }

    /**
     * [DeploymentFailure] that can happen when a preinstallation fails.
     * @param message a message that better describes the error.
     */
    @Serializable
    @SerialName("PreInstallFailure")
    data class PreInstallFailure(
        override val message: String,
    ) : DeploymentFailure

    /**
     * [DeploymentFailure] that can happen when a postInstall fails.
     * @param message a message that better describes the error.
     */
    @Serializable
    @SerialName("PostInstallFailure")
    data class PostInstallFailure(
        override val message: String,
    ) : DeploymentFailure

    /**
     * [DeploymentFailure] that can happen during the installation process when deploying a [Software].
     * @param message a message that better describes the error.
     */
    @Serializable
    @SerialName("InstallFailure")
    data class InstallFailure(
        override val message: String,
    ) : DeploymentFailure {
        constructor(osCommandFailure: OsCommandFailure) : this(osCommandFailure.message)
    }

    /**
     * [DeploymentFailure] that can happen during the removal of software, if it is impossible to uninstall it.
     * @param message a message that better describes the error.
     */
    @Serializable
    @SerialName("UninstallFailure")
    data class UninstallFailure(
        override val message: String,
    ) : DeploymentFailure {
        constructor(osCommandFailure: OsCommandFailure) : this(osCommandFailure.message)
    }
}
