package it.unibo.osautoupdates.serialization.system.oscommand

import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Custom serializer for [OsCommand] that maps it to an object with a single string instead of an array for commands.
 * Won't support Bson serialization.
 */
actual object OsCommandPolymorphicSerializer : KSerializer<OsCommand> {
    private val delegate = JsonOsCommandPolymorphicSerializer

    actual override val descriptor: SerialDescriptor
        get() = delegate.descriptor

    actual override fun deserialize(decoder: Decoder): OsCommand = delegate.deserialize(decoder)

    actual override fun serialize(
        encoder: Encoder,
        value: OsCommand,
    ) = delegate.serialize(encoder, value)
}
