package it.unibo.osautoupdates.deployment

import it.unibo.osautoupdates.suite.ResolvedSuite

typealias Outcome = String

private fun includedSoftwareString(suite: ResolvedSuite): String =
    "${suite.flatten().map{ sw -> sw.specification().asString() }}"

fun successfulDeployOutcome(suite: ResolvedSuite): Outcome =
    "Deployment completed successfully, the following software are included: ${includedSoftwareString(suite)}"

/**
 * [Outcome] to return upon a failed deploy operation, in case the rollback is not enabled.
 */
fun failedDeployWithDisabledRollBack(): Outcome =
    "Deploy operation FAILED! Rollback was not performed as per configuration."

/**
 * Outcome for when the Downgrade process is requested but no Deploy was ever performed.
 * TODO: why is this customized for rollback to?
 */
fun noDeployWasPerformedOutcome(processName: String): Outcome =
    "No Suite have been successfully deployed yet. Skipping the $processName process."

/**
 * Outcome for when the process is requested but no successful Deploy was ever performed.
 */
fun noTargetAvailableForRetractOutcome(processName: String): Outcome =
    buildString {
        append(
            "$processName was successful, however there is no previous working state available. ",
        )
        append("Only the cleanup process was executed.")
    }

/**
 * Outcome for when the process is requested and is successful.
 * @param processName the name of the process, e.g. "Rollback" or "Downgrade".
 * @param activeSuite the currently active suite, which is the one that was deployed before the process started.
 */
fun successfulRetractOutcome(
    processName: String,
    activeSuite: ResolvedSuite,
): Outcome =
    "The $processName was successful, the following software are included :${includedSoftwareString(activeSuite)}"
