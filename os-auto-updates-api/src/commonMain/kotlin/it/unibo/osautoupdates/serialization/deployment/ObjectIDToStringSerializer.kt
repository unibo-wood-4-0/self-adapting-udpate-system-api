package it.unibo.osautoupdates.serialization.deployment

import it.unibo.osautoupdates.util.StringId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer for [StringId].
 */
expect object ObjectIDToStringSerializer : KSerializer<StringId> {
    override val descriptor: SerialDescriptor

    override fun deserialize(decoder: Decoder): StringId

    override fun serialize(
        encoder: Encoder,
        value: StringId,
    )
}
