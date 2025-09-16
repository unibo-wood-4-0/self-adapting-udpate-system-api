package it.unibo.osautoupdates.serialization.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Generic serializer for a [T] that can be converted to and from a string.
 * @param serialName the serial name of the serialized object.
 * @param decodeStringStrategy the strategy to convert a string to a [T].
 */
abstract class StringSurrogateSerializer<T>(
    serialName: String,
    private val decodeStringStrategy: (String) -> T,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            serialName,
            PrimitiveKind.STRING,
        )

    final override fun serialize(
        encoder: Encoder,
        value: T,
    ) {
        encoder.encodeString(value.toString())
    }

    final override fun deserialize(decoder: Decoder): T = decodeStringStrategy(decoder.decodeString())
}
