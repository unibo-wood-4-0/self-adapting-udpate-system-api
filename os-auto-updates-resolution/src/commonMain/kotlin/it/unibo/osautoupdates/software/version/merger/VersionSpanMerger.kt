package it.unibo.osautoupdates.software.version.merger

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import it.unibo.osautoupdates.failure.ResolutionFailure
import it.unibo.osautoupdates.software.version.Version
import it.unibo.osautoupdates.software.version.span.RangeVersion
import it.unibo.osautoupdates.software.version.span.SingleVersion
import it.unibo.osautoupdates.software.version.span.VersionSpan
import kotlin.ranges.rangeTo

/**
 * Merge two [it.unibo.osautoupdates.software.version.span.VersionSpan]s into a single one. The resulting [it.unibo.osautoupdates.software.version.span.VersionSpan] is the most detailed [it.unibo.osautoupdates.software.version.span.VersionSpan].
 * Usually this is done by intersecting the [it.unibo.osautoupdates.software.version.span.VersionSpan]s, but it is not always possible to do so.
 * For example, some [it.unibo.osautoupdates.software.version.span.RangeVersion] are impossible to intersect, because they are not compatible with each other.
 * In such cases, a [it.unibo.osautoupdates.failure.ResolutionFailure] is returned.
 * @param v1 the first [it.unibo.osautoupdates.software.version.span.VersionSpan] to merge.
 * @param v2 the second [it.unibo.osautoupdates.software.version.span.VersionSpan] to merge.
 */
internal data class VersionSpanMerger(
    private val v1: VersionSpan,
    private val v2: VersionSpan,
) {
    /**
     * Merge the two [it.unibo.osautoupdates.software.version.span.VersionSpan]s into a single one. The resulting [it.unibo.osautoupdates.software.version.span.VersionSpan] is the most detailed [it.unibo.osautoupdates.software.version.span.VersionSpan].
     * @return the most detailed [it.unibo.osautoupdates.software.version.span.VersionSpan] that is compatible with both [it.unibo.osautoupdates.software.version.span.VersionSpan]s or a [it.unibo.osautoupdates.failure.ResolutionFailure] if
     * it is not possible to create a compatible [it.unibo.osautoupdates.software.version.span.VersionSpan].
     */
    fun merge(): Either<ResolutionFailure, VersionSpan> =
        when (v2) {
            is SingleVersion ->
                when (v1) {
                    is SingleVersion ->
                        if (v1 == v2) {
                            v1.right()
                        } else {
                            ResolutionFailure.IncompatibleSingleVersions(v1, v2).left()
                        }
                    is RangeVersion<*> -> mergeSingleVersionInRangeVersion(v2, v1)
                }
            is RangeVersion<*> ->
                when (v1) {
                    is SingleVersion -> mergeSingleVersionInRangeVersion(v1, v2)
                    is RangeVersion<*> -> mergeRangeVersions(v1, v2)
                }
        }

    /**
     * The [it.unibo.osautoupdates.software.version.span.SingleVersion] is checked to be included in the [it.unibo.osautoupdates.software.version.span.RangeVersion] and not in the
     * [it.unibo.osautoupdates.software.version.span.RangeVersion.incompatibilities].
     * If it is, then the [it.unibo.osautoupdates.software.version.span.SingleVersion] is returned, otherwise a [it.unibo.osautoupdates.failure.ResolutionFailure] is returned.
     * @param v1 the [it.unibo.osautoupdates.software.version.span.SingleVersion] to check.
     * @param v2 the [it.unibo.osautoupdates.software.version.span.RangeVersion] to check.
     * @return the [it.unibo.osautoupdates.software.version.span.SingleVersion] if it is compatible with the [it.unibo.osautoupdates.software.version.span.RangeVersion], otherwise a
     * [it.unibo.osautoupdates.failure.ResolutionFailure.SingleVersionOutOfRange].
     */
    private fun mergeSingleVersionInRangeVersion(
        v1: SingleVersion,
        v2: RangeVersion<*>,
    ): Either<ResolutionFailure, VersionSpan> =
        when (v1.version in (v2.min..v2.max) && v1.version !in v2.incompatibilities) {
            true -> v1.right()
            false -> ResolutionFailure.SingleVersionOutOfRange(v1, v2).left()
        }

    /**
     * The two [it.unibo.osautoupdates.software.version.span.RangeVersion]s are intersected: the minimum between the two max is taken as the new max, the maximum
     * between the two min is taken as the new min. The selected final versions can not be outside the range of these
     * two selected versions. The incompatible versions are the union of the two incompatible [it.unibo.osautoupdates.software.version.span.RangeVersion]s.
     * The preferred version is then chosen as the maximum between the two preferred versions that are included in the
     * created range and that are not incompatible.
     * The selected max and min field are also checked for compatibility at this point. The values are mapped
     * to null if they are incompatible. An attempt to create a [it.unibo.osautoupdates.software.version.span.RangeVersion] is then made.
     * @param v1 the first [it.unibo.osautoupdates.software.version.span.RangeVersion] to merge.
     * @param v2 the second [it.unibo.osautoupdates.software.version.span.RangeVersion] to merge.
     * @return the most detailed [it.unibo.osautoupdates.software.version.span.VersionSpan] that is compatible with both [it.unibo.osautoupdates.software.version.span.RangeVersion]s.
     * If no [it.unibo.osautoupdates.software.version.span.VersionSpan] can be created, then [it.unibo.osautoupdates.failure.ResolutionFailure.IncompatibleRangeVersions] is returned.
     */
    private fun mergeRangeVersions(
        v1: RangeVersion<*>,
        v2: RangeVersion<*>,
    ): Either<ResolutionFailure, VersionSpan> {
        val v3Max = listOf(v1.max, v2.max).min()
        val v3Min = listOf(v1.min, v2.min).max()
        val onlyPossibleRange = v3Min..v3Max
        val v3Incompatibilities =
            (v1.incompatibilities + v2.incompatibilities)
                .filter {
                    it in onlyPossibleRange
                }.toSet()
        val v3CompatiblePref =
            listOf(v1.preferred, v2.preferred)
                .filter { it in onlyPossibleRange }
                .filterNot { v3Incompatibilities.contains(it) }
                .maxOrNull()
        val v3CompatibleMin = v3Min.takeUnless { v3Incompatibilities.contains(it) }
        val v3CompatibleMax = v3Max.takeUnless { v3Incompatibilities.contains(it) }
        return when (v3CompatibleMax to v3CompatibleMin) {
            null to null -> v3CompatiblePref.toEither()
            null to v3CompatibleMin ->
                versionRangeFromPreferredAndNonNullable(
                    v3CompatiblePref,
                    v3CompatibleMin,
                    v3Incompatibilities,
                )
            v3CompatibleMax to null ->
                versionRangeFromPreferredAndNonNullable(
                    v3CompatiblePref,
                    v3CompatibleMax,
                    v3Incompatibilities,
                )
            else -> {
                v3CompatibleMin ?: error("The minimum version cannot be null at this point.")
                v3CompatibleMax ?: error("The maximum version cannot be null at this point.")
                if (v3CompatibleMin == v3CompatibleMax) {
                    v3CompatibleMax.toEither()
                } else {
                    VersionSpan
                        .range(
                            v3CompatibleMin,
                            v3CompatibleMax,
                            v3CompatiblePref ?: v3CompatibleMax,
                            v3Incompatibilities.filter { it in v3CompatibleMin..v3CompatibleMax }.toSet(),
                        ).right()
                }
            }
        }
    }

    /**
     * Creates a [it.unibo.osautoupdates.software.version.Version] starting from a [it.unibo.osautoupdates.software.version.span.VersionSpan] fields that can be null.
     * The max and min fields are null-checked:
     * - if both are null, then the preferred field is returned (it could be null, but it does not matter,
     * if it is there's nothing left to do);
     * - if only one is null, a [it.unibo.osautoupdates.software.version.span.VersionSpan] is created with the preferred field as min or max, depending on which
     * one is null;
     * - if both are not null:
     *      * if they are equal, the [SingleVersion] is returned;
     *      * otherwise, a [RangeVersion] is created using the fields. If the preferred field is null, the max field
     *      is used as preferred. The incompatibilities are filtered to keep only the ones that are in the range.
     * @return the resulting [it.unibo.osautoupdates.software.version.span.VersionSpan] or a [it.unibo.osautoupdates.failure.ResolutionFailure] if it is not possible to create a [it.unibo.osautoupdates.software.version.span.VersionSpan].
     */
    private fun versionRangeFromPreferredAndNonNullable(
        v3CompatiblePref: Version?,
        nonNullVersion: Version?,
        v3Incompatibilities: Set<Version>,
    ): Either<ResolutionFailure, VersionSpan> {
        requireNotNull(nonNullVersion)
        return when (v3CompatiblePref) {
            null, nonNullVersion -> VersionSpan.single(nonNullVersion).right()
            else ->
                VersionSpan
                    .range(
                        if (v3CompatiblePref < nonNullVersion) v3CompatiblePref else nonNullVersion,
                        if (v3CompatiblePref < nonNullVersion) nonNullVersion else v3CompatiblePref,
                        v3CompatiblePref,
                        v3Incompatibilities.filter { it in nonNullVersion..v3CompatiblePref }.toSet(),
                    ).right()
        }
    }

    /**
     * Map the nullable [it.unibo.osautoupdates.software.version.Version] to an [arrow.core.Either].
     * @return the [it.unibo.osautoupdates.software.version.Version] if it was not null, or a [it.unibo.osautoupdates.failure.ResolutionFailure] if it was.
     */
    @Suppress("UNCHECKED_CAST")
    private fun Version?.toEither(): Either<ResolutionFailure, VersionSpan> =
        when (this) {
            null ->
                ResolutionFailure
                    .IncompatibleRangeVersions(
                        v1 as RangeVersion<Version>,
                        v2 as RangeVersion<Version>,
                        """
                        There was an attempt to recover and create a valid RangeVersion instance,
                        but it failed as the requested versions are impossible to merge.
                        """.trimIndent().replace("\n", " "),
                    ).left()
            else -> VersionSpan.single(this).right()
        }
}
