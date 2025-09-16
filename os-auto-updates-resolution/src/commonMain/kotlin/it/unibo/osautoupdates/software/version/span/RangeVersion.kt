package it.unibo.osautoupdates.software.version.span

import it.unibo.osautoupdates.software.version.Version
import kotlin.ranges.rangeTo
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A [VersionSpan] that contains a range of [it.unibo.osautoupdates.software.version.Version]s.
 * @param min the minimum [it.unibo.osautoupdates.software.version.Version] of the range.
 * @param max the maximum [it.unibo.osautoupdates.software.version.Version] of the range.
 * @param preferred the preferred [it.unibo.osautoupdates.software.version.Version] of the range.
 * @param incompatibilities the [it.unibo.osautoupdates.software.version.Version]s that are incompatible with the range.
 */
@Serializable(with = RangeVersionSerializer::class)
@SerialName(RangeVersion.SERIAL_NAME)
data class RangeVersion<out V : Version>(
    val min: V,
    val max: V,
    val preferred: V,
    val incompatibilities: Set<V>,
) : VersionSpan {
    init {
        require(
            incompatibilities.all { it::class == min::class } &&
                preferred::class == min::class &&
                max::class == min::class,
        ) {
            "The specified versions are of different types. They should all be consistent."
        }
        require(max != min) {
            """
            The specified max version ($max) is equal to the min version ($min).
            If you want to specify a single version, just use a SingleVersion instead."
            """.trimIndent()
        }
        require(max > min) {
            "The specified max version ($max) is not greater than the min version ($min)."
        }
        require(preferred in min..max) {
            "The specified preferred version ($preferred) is not in inclusive range [$min - $max]."
        }
        incompatibilities.filterNot { it > min && it < max }.let {
            require(it.isEmpty()) {
                "Some version specified as incompatible ($it) are not in inclusive range [$min - $max]"
            }
        }
        require(incompatibilities.none { it == preferred }) {
            "The preferred version ($preferred) cannot be incompatible."
        }
    }

    /**
     * Utility for [RangeVersion] class.
     */
    companion object {
        /**
         * The name of the [RangeVersion] type, used for serialization.
         */
        const val SERIAL_NAME = "Range"
    }
}

/**
 * Serializer for [RangeVersion] that uses the [it.unibo.osautoupdates.software.version.Version] serializer for each field.
 */
@Suppress("UNCHECKED_CAST")
class RangeVersionSerializer<V : Version> : KSerializer<RangeVersion<V>> {
    @Serializable
    @SerialName("Range")
    private data class InternalRangeVersion(
        val min: Version,
        val max: Version,
        val preferred: Version = max,
        val incompatibilities: Set<Version> = emptySet(),
    )

    override val descriptor: SerialDescriptor get() = InternalRangeVersion.serializer().descriptor

    override fun serialize(
        encoder: Encoder,
        value: RangeVersion<V>,
    ) {
        InternalRangeVersion.serializer().serialize(
            encoder,
            InternalRangeVersion(
                value.min,
                value.max,
                value.preferred,
                value.incompatibilities,
            ),
        )
    }

    override fun deserialize(decoder: Decoder): RangeVersion<V> {
        val internal = InternalRangeVersion.serializer().deserialize(decoder)
        return RangeVersion(
            internal.min as V,
            internal.max as V,
            internal.preferred as V,
            internal.incompatibilities as Set<V>,
        )
    }
}
