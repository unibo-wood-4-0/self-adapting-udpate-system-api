package it.unibo.osautoupdates.serialization.system.oscommand

import arrow.core.nonEmptySetOf
import it.unibo.osautoupdates.system.oscommand.OsCommand
import it.unibo.osautoupdates.system.oscommand.SuccessfulStrategy
import it.unibo.osautoupdates.system.oscommand.SuccessfulStrategy.SuccessfulCodes
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * [OsCommand] [KSerializer] that serialize all the information as in the original object.
 */
object DefaultOsCommandSerializer : KSerializer<OsCommand> {
    @SerialName(OsCommand.SERIAL_NAME)
    @Serializable
    private data class OriginalOsCommand(
        val commands: List<String>,
        val successfulStrategy: SuccessfulStrategy = SuccessfulCodes(nonEmptySetOf(0)),
    )

    private val delegate = OriginalOsCommand.serializer()

    override val descriptor: SerialDescriptor
        get() = delegate.descriptor

    override fun deserialize(decoder: Decoder): OsCommand {
        delegate.deserialize(decoder).let {
            return OsCommand(it.commands, it.successfulStrategy)
        }
    }

    override fun serialize(
        encoder: Encoder,
        value: OsCommand,
    ) {
        delegate.serialize(encoder, OriginalOsCommand(value.commands, value.successfulStrategy))
    }
}
