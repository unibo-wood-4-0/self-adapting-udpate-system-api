package it.unibo.osautoupdates.serialization.system.oscommand

import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Custom serializer for [OsCommand] that maps it to an array of strings.
 * @see OsCommand
 * @see OsCommandStringSerializer
 */
data object OsCommandAsArraySerializer : KSerializer<OsCommand> {
    private val listSerializer = ListSerializer(String.serializer())
    override val descriptor: SerialDescriptor get() = listSerializer.descriptor

    override fun deserialize(decoder: Decoder): OsCommand = OsCommand(decoder.decodeSerializableValue(listSerializer))

    override fun serialize(
        encoder: Encoder,
        value: OsCommand,
    ) {
        encoder.encodeSerializableValue(listSerializer, value.commands)
    }
}
