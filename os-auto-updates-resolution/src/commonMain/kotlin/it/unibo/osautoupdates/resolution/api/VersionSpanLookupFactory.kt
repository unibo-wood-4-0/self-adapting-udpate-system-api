package it.unibo.osautoupdates.resolution.api

import arrow.core.EitherNel
import arrow.core.Nel
import arrow.core.getOrElse
import arrow.core.raise.Raise
import it.unibo.osautoupdates.failure.ResolutionFailure
import it.unibo.osautoupdates.software.ReleasedSoftware
import it.unibo.osautoupdates.software.Software
import it.unibo.osautoupdates.software.SoftwareName
import it.unibo.osautoupdates.software.version.span.VersionSpan
import it.unibo.osautoupdates.suite.ReleasedSuite

private typealias UnsimplifiedVersionSpanLookup = Map<SoftwareName, Set<VersionSpan>>

/**
 * Factory that creates [VersionSpanLookup].
 */
object VersionSpanLookupFactory {
    /**
     * Create a [VersionSpanLookup] from a [ReleasedSuite].
     * @see fromSoftware
     */
    context(_: Raise<Nel<ResolutionFailure>>)
    fun fromSuite(suite: ReleasedSuite): VersionSpanLookup = fromSoftware(suite)

    /**
     * Create a [VersionSpanLookup] from a collection of [Software].
     * The [Software]'s [it.unibo.osautoupdates.software.version.merger.span.VersionSpan] are collected and merged together to create a single [it.unibo.osautoupdates.software.version.merger.span.VersionSpan] for each
     * [Software]. Since this operation can fail, the result is an [EitherNel] that contains of [it.unibo.osautoupdates.ResolutionFailure]s
     * in case of failure for each error that occurred during the process.
     * @param software the [Software] to use to create the [VersionSpanLookup].
     */
    context(_: Raise<Nel<ResolutionFailure>>)
    fun fromSoftware(software: Collection<ReleasedSoftware>): VersionSpanLookup {
        if (software.isEmpty()) {
            return emptyMap()
        }
        val mergedUnsimplified =
            mergeUnsimplifiedLookups(
                software.map {
                    unmergedLookup(it)
                },
            ).mapValues { VersionSpan.merge(it.value) }
        val errors: List<ResolutionFailure> = mergedUnsimplified.mapNotNull { it.value.leftOrNull() }
        require(errors.isEmpty()) {
            """
            Creation of VersionSpanLookup failed because of a logical bug in the application.
            No errors should be present at this point.
            """.trimIndent()
        }
        return mergedUnsimplified.mapValues {
            it.value.getOrElse {
                error("A generic and not properly handled error occurred during VersionSpanLookup creation.")
            }
        }
    }

    private fun unmergedLookup(sw: ReleasedSoftware): Map<SoftwareName, Set<VersionSpan>> {
        val swMap = mapOf(sw.name to setOf(sw.versionSpan))
        return if (sw.dependencies.isEmpty()) {
            swMap
        } else {
            mergeUnsimplifiedLookups(sw.dependencies.map { unmergedLookup(it) } + swMap)
        }
    }

    private fun mergeUnsimplifiedLookups(maps: List<UnsimplifiedVersionSpanLookup>): UnsimplifiedVersionSpanLookup =
        maps.reduce { acc, map ->
            acc + map.mapValues { (k, v) -> v + (acc[k].orEmpty()) }
        }
}
