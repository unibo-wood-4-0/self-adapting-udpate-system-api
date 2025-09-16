package it.unibo.osautoupdates.serialization.system.oscommand

import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bson.codecs.kotlinx.BsonDecoder
import org.bson.codecs.kotlinx.BsonEncoder

/**
 * Custom serializer for [OsCommand] that maps it to an object with a single string instead of an array for commands.
 * This custom serializer also works with [BsonEncoder] and [BsonDecoder].
 * In case of Bson serialization, [OsCommandArrayCodeSerializer] will be used.
 * The [JsonOsCommandPolymorphicSerializer] will be used in all other cases.
 */
@OptIn(ExperimentalSerializationApi::class)
actual object OsCommandPolymorphicSerializer : KSerializer<OsCommand> {
    private val polymorphicJsonSerializer = JsonOsCommandPolymorphicSerializer

    actual override val descriptor: SerialDescriptor
        get() = polymorphicJsonSerializer.descriptor

    actual override fun deserialize(decoder: Decoder): OsCommand =
        when (decoder) {
            is BsonDecoder -> DefaultOsCommandSerializer.deserialize(decoder)
            else -> polymorphicJsonSerializer.deserialize(decoder)
        }

    actual override fun serialize(
        encoder: Encoder,
        value: OsCommand,
    ) {
        when (encoder) {
            is BsonEncoder -> DefaultOsCommandSerializer.serialize(encoder, value)
            else -> polymorphicJsonSerializer.serialize(encoder, value)
        }
    }
}
