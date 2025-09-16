package it.unibo.osautoupdates.serialization.deployment

import it.unibo.osautoupdates.util.StringId
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bson.codecs.kotlinx.BsonDecoder
import org.bson.codecs.kotlinx.BsonEncoder
import org.bson.types.ObjectId

/**
 * Serializer for [StringId].
 * Also take care of the serialization and deserialization of [ObjectId] in BSON.
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
    ) = when (encoder) {
        is BsonEncoder -> encoder.encodeObjectId(ObjectId(value.value))
        else -> encoder.encodeString(value.value)
    }

    actual override fun deserialize(decoder: Decoder): StringId =
        StringId(
            when (decoder) {
                is BsonDecoder -> decoder.decodeObjectId().toHexString()
                else -> decoder.decodeString()
            },
        )
}
