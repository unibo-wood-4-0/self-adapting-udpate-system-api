package it.unibo.osautoupdates.serialization.system.oscommand

import it.unibo.osautoupdates.system.oscommand.OsCommand
import it.unibo.osautoupdates.system.oscommand.SuccessfulStrategy.Success
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Custom serializer for [OsCommand] that maps it to an object with a single string instead of an array for commands.
 * Example: {"command": ["ls", "-l"], "success": "true" }
 */
object OsCommandArraySuccessSerializer : KSerializer<OsCommand> {
    @Serializable
    @SerialName(OsCommand.SERIAL_NAME)
    private data class InternalOsCommand(
        val commands: List<String>,
        val success: Boolean,
    )

    private val delegateSerializer = InternalOsCommand.serializer()

    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    override fun deserialize(decoder: Decoder): OsCommand =
        delegateSerializer.deserialize(decoder).let {
            OsCommand(it.commands, Success(it.success))
        }

    override fun serialize(
        encoder: Encoder,
        value: OsCommand,
    ) {
        val successStrategy = value.successfulStrategy
        require(successStrategy is Success) {
            "Incompatible Success Strategy $successStrategy, expected type ${Success::class.simpleName}."
        }
        delegateSerializer.serialize(
            encoder,
            InternalOsCommand(value.commands, successStrategy.success),
        )
    }
}
