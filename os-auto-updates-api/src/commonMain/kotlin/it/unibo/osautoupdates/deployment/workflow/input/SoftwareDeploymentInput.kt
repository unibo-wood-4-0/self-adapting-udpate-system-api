@file:UseSerializers(NonEmptyListSerializer::class)

package it.unibo.osautoupdates.deployment.workflow.input

import arrow.core.Nel
import arrow.core.nonEmptyListOf
import arrow.core.serialization.NonEmptyListSerializer
import it.unibo.osautoupdates.failure.DeploymentFailure
import it.unibo.osautoupdates.failure.Failure
import it.unibo.osautoupdates.failure.SoftFailure
import it.unibo.osautoupdates.software.version.Version
import it.unibo.osautoupdates.validation.ValidationResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

/**
 * The event that indicates that the software is checked for already being installed.
 */
@Serializable
@SerialName("CheckAlreadyInstalled")
sealed interface CheckAlreadyInstalled : Input {
    /**
     * The event that indicates that the software is not already installed.
     */
    @Serializable
    @SerialName("NotInstalled")
    data object IsNotInstalled : CheckAlreadyInstalled

    /**
     * The event that indicates that the software is already installed.
     */
    @Serializable
    @SerialName("IsInstalled")
    sealed interface IsInstalled : CheckAlreadyInstalled {
        /**
         * The event that indicates that the software is already installed with the same version.
         */
        @Serializable
        @SerialName("IsInstalled (SameVersion)")
        data class SameVersion(
            private val version: Version,
        ) : IsInstalled

        /**
         * The event that indicates that the software is already installed with a different version.
         */
        @Serializable
        @SerialName("IsInstalled (DifferentVersion)")
        data class DifferentVersion(
            private val installed: Version,
            private val desired: Version,
        ) : IsInstalled
    }
}

/**
 * The event that indicates that the software is being removed or brought back to the previous state,
 * probably because of a failure.
 */
@Serializable
@SerialName("Remove")
data object Remove : Input

/**
 * The event that indicates that the software has been validated.
 * @param validationResult The result of the validation.
 */
@Serializable
@SerialName("Validated")
data class Validate(
    val validationResult: ValidationResult,
) : Input

/**
 * The event that indicates that one or more [Failure]s occurred during the deployment process.
 * @param failures The list of [DeploymentFailure]s that occurred.
 */
@Serializable
@SerialName("Fail")
data class Fail(
    val failures: Nel<SoftFailure>,
) : Input {
    constructor(
        failure: SoftFailure,
        vararg failures: SoftFailure,
    ) : this(nonEmptyListOf(failure, *failures))
}

/**
 * The event that indicates that the deployment process was successful.
 */
@Serializable
@SerialName("Success")
data object Success : Input
