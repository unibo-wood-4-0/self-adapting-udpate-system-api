@file:Suppress("UNCHECKED_CAST")

package it.unibo.osautoupdates.software.version.span

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import it.unibo.osautoupdates.failure.ResolutionFailure
import it.unibo.osautoupdates.software.version.Version
import it.unibo.osautoupdates.software.version.Version.Companion.toVersion
import it.unibo.osautoupdates.software.version.merger.VersionSpanMerger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A type of [it.unibo.osautoupdates.software.version.Version] that represent a possible range of values.
 * This entity is useful to declare a span of possible versions for a [Software].
 * @param V the type of [it.unibo.osautoupdates.software.version.Version] contained in the [VersionSpan].
 */

@Serializable
@SerialName(VersionSpan.SERIAL_NAME)
sealed interface VersionSpan {
    /**
     * Utility for the [VersionSpan] class.
     */
    companion object {
        /**
         * The name of the [VersionSpan] type, used for serialization.
         */
        const val SERIAL_NAME = "VersionSpan"

        /**
         * Static factory that creates a [VersionSpan] using a single version.
         * @return a [VersionSpan] with a [V] version type.
         */
        fun <V : Version> single(version: V): VersionSpan = SingleVersion(version)

        /**
         * Static factory that creates a [VersionSpan] using a single version.
         * @return a [String] representing a [it.unibo.osautoupdates.software.version.Version] that will be parsed.
         * @return a [VersionSpan] with a [V] version type.
         */
        fun singleFromString(string: String): VersionSpan = single(string.toVersion())

        /**
         * Static factory that creates a [VersionSpan] using a range of versions.
         */
        fun <V : Version> range(
            min: V,
            max: V,
            preferred: V = max,
            incompatibilities: Set<V> = emptySet(),
        ): VersionSpan = RangeVersion(min, max, preferred, incompatibilities)

        /**
         * Static factory that creates a [VersionSpan] using a range of versions using [String]s.
         */
        fun rangeFromString(
            min: String,
            max: String,
            preferred: String = max,
            incompatibilities: Set<String> = emptySet(),
        ): VersionSpan =
            range(
                min.toVersion(),
                max.toVersion(),
                preferred.toVersion(),
                incompatibilities.map { it.toVersion() }.toSet(),
            )

        /**
         * Merge a collection of [VersionSpan] into a single [VersionSpan].
         * @param versionSpans the [VersionSpan]s to merge.
         * @return the [VersionSpan] obtained by merging the given [VersionSpan] as [arrow.core.Either.Right].
         * If it is impossible to merge the [VersionSpan]s, A [ResolutionFailure] is return instead as [arrow.core.Either.Left].
         */

        fun merge(versionSpans: Collection<VersionSpan>): Either<ResolutionFailure, VersionSpan> =
            when (versionSpans.size) {
                0 -> ResolutionFailure.NoElementToResolve.left()
                1 -> versionSpans.first().right()
                else -> {
                    val list: List<Either<ResolutionFailure, VersionSpan>> = versionSpans.map { it.right() }
                    list.reduce { acc, v ->
                        acc.flatMap { prev ->
                            v.flatMap { next -> VersionSpanMerger(prev, next).merge() }
                        }
                    }
                }
            }

        /**
         * Merge a collection of [VersionSpan] into a single [VersionSpan].
         * @param versionSpans the [VersionSpan]s to merge.
         * @return the [VersionSpan] obtained by merging the given [VersionSpan] as [arrow.core.Either.Right].
         * If it is impossible to merge the [VersionSpan]s, A [ResolutionFailure] is return instead as [arrow.core.Either.Left].
         */

        fun mergeAll(vararg versionSpans: VersionSpan): Either<ResolutionFailure, VersionSpan> =
            merge(versionSpans.toList())
    }
}
