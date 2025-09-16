package it.unibo.osautoupdates.serialization.system.oscommand

import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

/**
 * Custom serializer for [OsCommand] that maps it to an object with a single string instead of an array for commands.
 * This custom serializer also works with [BsonEncoder] and [BsonDecoder].
 * In case of Bson serialization, [OsCommandArrayCodeSerializer] will be used.
 * The [JsonOsCommandPolymorphicSerializer] will be used in all other cases.
 */
expect object OsCommandPolymorphicSerializer : KSerializer<OsCommand> {
    override val descriptor: SerialDescriptor

    override fun deserialize(decoder: Decoder): OsCommand

    override fun serialize(
        encoder: Encoder,
        value: OsCommand,
    )
}

/**
 * The [OsCommand] polymorphic serializer that can deserialize the [OsCommand]:
 * - from a single string (e.g., "ls -l")
 * - from a list of strings (e.g., ["ls", "-l"]);
 * - from an object, any of the following work:
 *      (e.g., {"commands": ["ls", "-l"], "successfulCodes": [0, 2] }).
 *      (e.g., {"command": "ls -l", "successfulCodes": [0, 2] }).
 *      (e.g., {"commands": "ls -l", "success": "true" }).
 *      (e.g., {"commands": ["ls", "-l"], "success": "true" }).
 * @see OsCommandStringSerializer
 * @see OsCommand
 */
object JsonOsCommandPolymorphicSerializer : JsonContentPolymorphicSerializer<OsCommand>(OsCommand::class) {
    override fun selectDeserializer(element: JsonElement) =
        when {
            element is JsonPrimitive && element.isString -> OsCommandStringSerializer
            element is JsonArray -> OsCommandAsArraySerializer
            element is JsonObject -> osCommandObjectSerializer(element.jsonObject)
            else -> throw SerializationException(
                "OsCommands can only be deserialized from strings, arrays and objects.",
            )
        }

    private fun osCommandObjectSerializer(jsonObject: JsonObject): KSerializer<OsCommand> {
        // Validation checks
        val hasKey: (String) -> Boolean = { key -> key in jsonObject }

        val commands = hasKey("commands")
        val command = hasKey("command")
        val successfulCodes = hasKey("successfulCodes")
        val success = hasKey("success")

        isSyntaxValid(commands, command, success, successfulCodes)

        // Determine serializer
        return when {
            command && success -> OsCommandStringSuccessSerializer
            commands && success -> OsCommandArraySuccessSerializer
            command && successfulCodes -> OsCommandStringCodeSerializer
            commands && successfulCodes -> OsCommandArrayCodeSerializer
            commands -> DefaultOsCommandSerializer
            else -> throw SerializationException(
                """
                Could not determine the correct serializer for the OsCommand object.
                Use a simple string if you want to specify a single command that succeeds on any exit code.
                It is also possible to use a more complex object with 'command' and 'successfulCodes' keys.
                {
                    command": "ls -l",
                    successfulCodes": [0, 2]
                }
                """.trimIndent(),
            )
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun isSyntaxValid(
        hasCommands: Boolean,
        hasCommand: Boolean,
        hasSuccess: Boolean,
        hasSuccessfulCodes: Boolean,
    ) {
        when {
            hasCommands && hasCommand -> throw SerializationException(
                "Specify only one of 'commands' or 'command' in the OsCommand object.",
            )
            hasSuccess && hasSuccessfulCodes -> throw SerializationException(
                "Specify only one of 'success' or 'successfulCodes' in the OsCommand object.",
            )
            !hasCommands && !hasCommand -> throw MissingFieldException(
                "commands",
                "Either specify 'commands' or 'command' as keys, along with 'successfulCodes' or 'success'.",
            )
        }
    }
}
