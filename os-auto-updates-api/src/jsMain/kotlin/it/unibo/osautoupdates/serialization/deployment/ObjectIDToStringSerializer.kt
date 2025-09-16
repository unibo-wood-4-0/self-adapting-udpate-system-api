package it.unibo.osautoupdates.serialization.deployment

import it.unibo.osautoupdates.util.StringId
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer for [StringId].
 */
@ExperimentalSerializationApi
actual object ObjectIDToStringSerializer : KSerializer<StringId> {
    actual override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            "ObjectIdSerializer",
            PrimitiveKind.STRING,
        )

    actual override fun serialize(
        encoder: Encoder,
        value: StringId,
    ) = encoder.encodeString(value.value)

    actual override fun deserialize(decoder: Decoder): StringId = StringId(decoder.decodeString())
}
