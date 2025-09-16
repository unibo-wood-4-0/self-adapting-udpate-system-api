@file:UseSerializers(NonEmptyListSerializer::class)

package it.unibo.osautoupdates.failure

import arrow.core.Nel
import arrow.core.nonEmptyListOf
import arrow.core.serialization.NonEmptyListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

/**
 * [Failure] that indicate that the current device state could not be recovered automatically.
 * This is the worst case scenario, where the system is in an inconsistent state and it was not possible to
 * recover it automatically.
 */
@Serializable
@SerialName("CriticalFailure")
sealed interface CriticalFailure : Failure {
    /**
     * [CriticalFailure] that indicate a failure during the downgrade process.
     * @param failures the list of failures that occurred during the downgrade.
     */
    @Serializable
    @SerialName("DowngradeFailure")
    data class DowngradeFailure(
        val failures: Nel<SoftFailure>,
    ) : CriticalFailure {
        constructor(f: SoftFailure, vararg fs: SoftFailure) : this(nonEmptyListOf(f, *fs))

        override val message: String = failureMessage("Downgrade", failures)
    }

    /**
     * [CriticalFailure] that indicate a failure during the rollback process.
     * @param failures the list of failures that occurred during the rollback.
     */
    @Serializable
    @SerialName("RollbackFailure")
    data class RollbackFailure(
        val failures: Nel<SoftFailure>,
    ) : CriticalFailure {
        constructor(f: SoftFailure, vararg fs: SoftFailure) : this(nonEmptyListOf(f, *fs))

        override val message: String = failureMessage("Rollback", failures)
    }

    /**
     * [DeploymentFailure] for a generic failure that could not be typed, for example an exception.
     * @param message a message that better describes the error.
     */
    @Serializable
    @SerialName("ExceptionFailure")
    data class ExceptionFailure(
        override val message: String,
    ) : CriticalFailure {
        constructor(exception: Throwable) : this(exceptionFailureMessage(exception))
    }

    /**
     * [CriticalFailure] that indicates that the local repository is unreachable.
     * With no local repository, the system cannot memorize the necessary state.
     * This is a critical failure because it prevents the system from functioning correctly.
     * @param message a message that better describes the error.
     */
    data class UnreachableLocalRepository(
        override val message: String,
    ) : CriticalFailure {
        companion object {
            /**
             * Utility mapping function to convert an [IOFailure.RepositoryInteraction] to a
             * [UnreachableLocalRepository].
             */
            fun IOFailure.RepositoryInteraction.toCriticalUnreachableRepoFailure(): UnreachableLocalRepository =
                UnreachableLocalRepository("Unreachable local repository: $message")
        }
    }
}

/**
 * Message to be used when an exception occurs.
 */
private val exceptionFailureMessage: (Throwable) -> String = { exception ->
    """
    An exception occurred:${exception::class.simpleName}: ${exception.message}
    ${exception.stackTraceToString()}
    """.trimIndent()
}

private fun failureMessage(
    processName: String,
    failures: Nel<SoftFailure>,
): String =
    "Failures occurred while executing the $processName process:" +
        failures.joinToString("\n") { it.message }
