@file:UseSerializers(NonEmptySetSerializer::class)

package it.unibo.osautoupdates.serialization.system.oscommand

import arrow.core.serialization.NonEmptySetSerializer
import it.unibo.osautoupdates.system.oscommand.OsCommand
import it.unibo.osautoupdates.system.oscommand.SuccessfulStrategy.SuccessfulCodes
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Custom serializer for [OsCommand] that maps it to an object with a single string instead of an array for commands.
 * Example: {"command": ["ls", "-l"], "successfulCodes": [0, 2] }
 */
object OsCommandArrayCodeSerializer : KSerializer<OsCommand> {
    @Serializable
    @SerialName(OsCommand.SERIAL_NAME)
    private class InternalOsCommand {
        val commands: List<String>
        val successfulCodes: Set<Int>

        constructor(osCommand: OsCommand) {
            val strategy = osCommand.successfulStrategy
            require(strategy is SuccessfulCodes) {
                "Incompatible Success Strategy $strategy, expected type ${SuccessfulCodes::class.simpleName}."
            }
            this.commands = osCommand.commands
            this.successfulCodes = strategy.successfulCodes
        }
    }

    private val delegateSerializer = InternalOsCommand.serializer()

    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    override fun deserialize(decoder: Decoder): OsCommand =
        delegateSerializer.deserialize(decoder).let {
            OsCommand(it.commands, SuccessfulCodes(it.successfulCodes))
        }

    override fun serialize(
        encoder: Encoder,
        value: OsCommand,
    ) {
        delegateSerializer.serialize(encoder, InternalOsCommand(value))
    }
}
