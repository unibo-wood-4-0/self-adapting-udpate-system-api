package it.unibo.osautoupdates.serialization.version

import it.unibo.osautoupdates.software.version.Version
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer for the [Version] class.
 * It serializes the [Version] as a [String].
 */
object VersionAsStringSerializer : KSerializer<Version> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(Version.SERIAL_NAME, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Version = Version.fromString(decoder.decodeString())

    override fun serialize(
        encoder: Encoder,
        value: Version,
    ) = encoder.encodeString(value.toString())
}
