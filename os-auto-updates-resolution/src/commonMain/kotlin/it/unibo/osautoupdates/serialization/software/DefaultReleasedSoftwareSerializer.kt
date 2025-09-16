package it.unibo.osautoupdates.serialization.software

import it.unibo.osautoupdates.ds.DeploymentStrategy
import it.unibo.osautoupdates.software.ReleasedSoftware
import it.unibo.osautoupdates.software.version.NotDefined
import it.unibo.osautoupdates.software.version.span.SingleVersion
import it.unibo.osautoupdates.software.version.span.VersionSpan
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Default [kotlinx.serialization.KSerializer] for [it.unibo.osautoupdates.software.ReleasedSoftware].
 */
object DefaultReleasedSoftwareSerializer : KSerializer<ReleasedSoftware> {
    @Serializable
    @SerialName(ReleasedSoftware.SERIAL_NAME)
    private data class InternalReleasedSoftware(
        val name: String,
        val validationTests: List<OsCommand> = emptyList(),
        val deploymentStrategies: List<DeploymentStrategy>,
        val dependencies: Set<ReleasedSoftware> = emptySet(),
        val versionSpan: VersionSpan = SingleVersion(NotDefined),
    )

    private val delegate = InternalReleasedSoftware.serializer()

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun deserialize(decoder: Decoder): ReleasedSoftware {
        val delegateClass = decoder.decodeSerializableValue(delegate)
        return ReleasedSoftware(
            delegateClass.name,
            delegateClass.validationTests,
            delegateClass.deploymentStrategies,
            delegateClass.dependencies,
            delegateClass.versionSpan,
        )
    }

    override fun serialize(
        encoder: Encoder,
        value: ReleasedSoftware,
    ) {
        val delegateClass =
            InternalReleasedSoftware(
                value.name,
                value.validationTests,
                value.deploymentStrategies,
                value.dependencies,
                value.versionSpan,
            )
        encoder.encodeSerializableValue(delegate, delegateClass)
    }
}
