package it.unibo.osautoupdates.serialization.software

import it.unibo.osautoupdates.ds.DeploymentStrategy
import it.unibo.osautoupdates.software.ReleasedSoftware
import it.unibo.osautoupdates.software.SoftwareName
import it.unibo.osautoupdates.software.version.Version
import it.unibo.osautoupdates.software.version.span.RangeVersion
import it.unibo.osautoupdates.software.version.span.SingleVersion
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * Check the string and return true if it contains any of the specified strings to select the correct deserializer.
 */
fun useReleasedSerializer(string: String): Boolean =
    listOf("versionRange", "versionSpan").any {
        string.contains(it)
    }

/**
 * Polymorphic serializer for [it.unibo.osautoupdates.software.ReleasedSoftware], uses different deserializers based on the content of the JSON.
 */
object JsonReleasedSoftwarePolymorphicSerializer : JsonContentPolymorphicSerializer<ReleasedSoftware>(
    ReleasedSoftware::class,
) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ReleasedSoftware> {
        require(element is JsonObject)
        val hasVersion = element.containsKey("version")
        val hasVersionRange = element.containsKey("versionRange")
        return when {
            hasVersion && hasVersionRange -> throw SerializationException(
                "Both 'version' and 'versionRange' are present in the JSON, only one should be present.",
            )
            hasVersion -> SingleVersionSoftwareSerializer
            hasVersionRange -> RangeVersionSoftwareSerializer
            else -> DefaultReleasedSoftwareSerializer
        }
    }
}

private object SingleVersionSoftwareSerializer : KSerializer<ReleasedSoftware> {
    @Serializable
    @SerialName(ReleasedSoftware.SERIAL_NAME)
    data class InternalSingleVersionSoftware(
        val name: SoftwareName,
        val validationTests: List<OsCommand> = emptyList(),
        val deploymentStrategies: List<DeploymentStrategy>,
        val dependencies: Set<ReleasedSoftware> = emptySet(),
        val version: Version,
    )

    val delegate = InternalSingleVersionSoftware.serializer()

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun deserialize(decoder: Decoder): ReleasedSoftware {
        val delegateClass = decoder.decodeSerializableValue(delegate)
        return ReleasedSoftware(
            delegateClass.name,
            delegateClass.validationTests,
            delegateClass.deploymentStrategies,
            delegateClass.dependencies,
            SingleVersion(delegateClass.version),
        )
    }

    override fun serialize(
        encoder: Encoder,
        value: ReleasedSoftware,
    ) {
        val delegateClass =
            InternalSingleVersionSoftware(
                value.name,
                value.validationTests,
                value.deploymentStrategies,
                value.dependencies,
                (value.versionSpan as SingleVersion).version,
            )
        encoder.encodeSerializableValue(delegate, delegateClass)
    }
}

private object RangeVersionSoftwareSerializer : KSerializer<ReleasedSoftware> {
    @Serializable
    @SerialName(ReleasedSoftware.SERIAL_NAME)
    data class InternalRangeVersionSoftware(
        val name: SoftwareName,
        val validationTests: List<OsCommand> = emptyList(),
        val deploymentStrategies: List<DeploymentStrategy>,
        val dependencies: Set<ReleasedSoftware> = emptySet(),
        val versionRange: RangeVersion<Version>,
    )

    val delegate = InternalRangeVersionSoftware.serializer()

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun deserialize(decoder: Decoder): ReleasedSoftware {
        val delegateClass = decoder.decodeSerializableValue(delegate)
        return ReleasedSoftware(
            delegateClass.name,
            delegateClass.validationTests,
            delegateClass.deploymentStrategies,
            delegateClass.dependencies,
            delegateClass.versionRange,
        )
    }

    override fun serialize(
        encoder: Encoder,
        value: ReleasedSoftware,
    ) {
        val delegateClass =
            InternalRangeVersionSoftware(
                value.name,
                value.validationTests,
                value.deploymentStrategies,
                value.dependencies,
                value.versionSpan as RangeVersion<Version>,
            )
        encoder.encodeSerializableValue(delegate, delegateClass)
    }
}
