package it.unibo.osautoupdates.failure

import it.unibo.osautoupdates.software.version.Version
import it.unibo.osautoupdates.software.version.span.RangeVersion
import it.unibo.osautoupdates.software.version.span.SingleVersion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [SoftFailure] that can happen during the resolution step.
 */
@Serializable
@SerialName("ResolutionFailure")
sealed interface ResolutionFailure {
    /**
     * A human-readable message that describes the [ResolutionFailure].
     */
    val message: String

    /**
     * Most general [ResolutionFailure] that happens when the resolution process could not be completed.
     */
    @Serializable
    @SerialName("CouldNotResolve")
    data class CouldNotResolve(
        override val message: String,
    ) : ResolutionFailure

    /**
     * [ResolutionFailure] that happens when trying to resolve the [it.unibo.osautoupdates.software.version.Version]
     * of an empty list of Software.
     */
    @Serializable
    @SerialName("NoElementToResolve")
    data object NoElementToResolve : ResolutionFailure {
        override val message: String = "There are no elements to resolve."
    }

    /**
     * [ResolutionFailure] that happens when two [it.unibo.osautoupdates.software.version.merger.span.SingleVersion]s declared for the same Software are different and
     * consequently incompatible.
     * Non-Working example:
     * Software A -> [it.unibo.osautoupdates.software.version.merger.span.SingleVersion] 1.0.0,
     * Software A -> [it.unibo.osautoupdates.software.version.merger.span.SingleVersion] 2.0.0.
     * Working example:
     * Software A -> [it.unibo.osautoupdates.software.version.merger.span.SingleVersion] 1.0.0
     * Software A -> [it.unibo.osautoupdates.software.version.merger.span.SingleVersion] 1.0.0.
     * This [ResolutionFailure] could also happen by declaring a
     * [it.unibo.osautoupdates.software.version.merger.span.RangeVersion] that somehow get simplified into a [it.unibo.osautoupdates.software.version.merger.span.SingleVersion].
     * @param first the first [it.unibo.osautoupdates.software.version.merger.span.SingleVersion] declared for the same
     * Software.
     * @param second the second [it.unibo.osautoupdates.software.version.merger.span.SingleVersion] declared for the same
     * Software.
     */
    @Serializable
    @SerialName("IncompatibleSingleVersions")
    data class IncompatibleSingleVersions(
        val first: SingleVersion,
        val second: SingleVersion,
    ) : ResolutionFailure {
        override val message: String = """
            Two versions declared for the same software are incompatible because they are different.
            The first version is: `$first`.
            The second version is: `$second`.
        """
    }

    /**
     * [ResolutionFailure] that happens when a [it.unibo.osautoupdates.software.version.merger.span.SingleVersion] is not included in a
     * [it.unibo.osautoupdates.software.version.merger.span.RangeVersion].
     * Consequently, the two versions are incompatible and impossible to merge.
     * Non-working example:
     * Software A -> [it.unibo.osautoupdates.software.version.merger.span.SingleVersion] 1.0.0,
     * Software A -> [it.unibo.osautoupdates.software.version.merger.span.RangeVersion] 2.0.0..3.0.0.
     * Working example: Software A -> [it.unibo.osautoupdates.software.version.merger.span.SingleVersion] 2.5.0,
     * Software A -> [it.unibo.osautoupdates.software.version.merger.span.RangeVersion] 2.0.0..3.0.0.
     * @param single the [it.unibo.osautoupdates.software.version.merger.span.SingleVersion] that is not included in the [it.unibo.osautoupdates.software.version.merger.span.RangeVersion].
     * @param range the [it.unibo.osautoupdates.software.version.merger.span.RangeVersion] that does not include the
     * [it.unibo.osautoupdates.software.version.merger.span.SingleVersion].
     */
    @Serializable
    @SerialName("SingleVersionOutOfRange")
    data class SingleVersionOutOfRange(
        val single: SingleVersion,
        val range: RangeVersion<*>,
    ) : ResolutionFailure {
        override val message: String = """
            Two versions declared for the same software are incompatible.
            The single version `$single` is not included in the range version `$range`.
        """
    }

    /**
     * [ResolutionFailure] that happens when two [it.unibo.osautoupdates.software.version.merger.span.RangeVersion]s are incompatible and consequently impossible to merge.
     * Non-working example: Software A -> [it.unibo.osautoupdates.software.version.merger.span.RangeVersion] 1.0.0..2.0.0, Software A -> [it.unibo.osautoupdates.software.version.merger.span.RangeVersion] 3.0.0..4.0.0.
     * Working example: Software A -> [it.unibo.osautoupdates.software.version.merger.span.RangeVersion] 1.0.0..2.0.0, Software A -> [it.unibo.osautoupdates.software.version.merger.span.RangeVersion] 1.5.0..4.0.0.
     * This [ResolutionFailure] can happen in a lot of different ways and could be hard to debug. The [Logs] field can
     * be used to better understand the problem.
     * Changing the configuration of the software to be more specific often helps to solve the problem.
     * @param first the first [it.unibo.osautoupdates.software.version.merger.span.RangeVersion] declared for the same Software.
     * @param second the second [it.unibo.osautoupdates.software.version.merger.span.RangeVersion] declared for the same Software.
     * @param additionalInfo additional information that can help to understand the problem.
     */
    @Serializable
    @SerialName("IncompatibleRangeVersions")
    data class IncompatibleRangeVersions(
        val first: RangeVersion<Version>,
        val second: RangeVersion<Version>,
        val additionalInfo: String,
    ) : ResolutionFailure {
        override val message: String = """
            There was an attempt to merge two different ranges of version declared for the same software.
            Please try to be more specific in the configuration of the software.
            The first version is: `$first`.
            The second version is: `$second`.
            Additional info: $additionalInfo
        """
    }
}
