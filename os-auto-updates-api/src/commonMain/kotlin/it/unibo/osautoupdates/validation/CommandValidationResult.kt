@file:UseSerializers(EitherSerializer::class, NonEmptyListSerializer::class)

package it.unibo.osautoupdates.validation

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.serialization.EitherSerializer
import arrow.core.serialization.NonEmptyListSerializer
import it.unibo.osautoupdates.failure.OsCommandFailure
import it.unibo.osautoupdates.system.oscommand.OsCommand
import it.unibo.osautoupdates.system.oscommand.OsCommandOutput
import it.unibo.osautoupdates.validation.CommandValidationResult.Companion.SERIAL_NAME
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeCollection

/**
 * Entity produced after the execution of validation tests.
 * @param results a [List] containing the results of each validation test executed on the software for each command.
 * @param dateTime the date and time when the validation tests were executed.
 */
@Serializable(with = CommandValidatorSerializer::class)
@SerialName(SERIAL_NAME)
data class CommandValidationResult(
    val results: Map<OsCommand, Either<OsCommandFailure, OsCommandOutput>>,
    override val dateTime: Instant = Clock.System.now(),
) : ValidationResult {
    override fun failedTests(): List<String> =
        results
            .filter { (_, value) ->
                value.getOrNull()?.isNotSuccessful() ?: true
            }.keys
            .map {
                it.asSingleString()
            }.toList()

    override fun isSuccessful(): Boolean =
        results.all { (_, value) ->
            value.getOrNull()?.isSuccessful() ?: false
        }

    /**
     * Utility for the [CommandValidationResult] class.
     */
    companion object {
        /**
         * The name used to serialize and deserialize a [CommandValidationResult].
         */
        const val SERIAL_NAME = "CommandValidationResult"

        /**
         * Static factory to create a [CommandValidationResult] with no results.
         * @return a [CommandValidationResult] with no results and the current time.
         */
        fun empty(): CommandValidationResult = CommandValidationResult(emptyMap(), Clock.System.now())
    }
}

object CommandValidatorSerializer : KSerializer<CommandValidationResult> {
    @Serializable
    @SerialName(SERIAL_NAME)
    private data class SerializableCommandValidationResult(
        val results: Map<String, Either<OsCommandFailure, OsCommandOutput>>,
        val dateTime: Instant,
    )

    override val descriptor: SerialDescriptor
        get() = SerializableCommandValidationResult.serializer().descriptor

    override fun serialize(
        encoder: Encoder,
        value: CommandValidationResult,
    ) {
        SerializableCommandValidationResult.serializer().serialize(
            encoder,
            SerializableCommandValidationResult(
                results = value.results.mapKeys { it.key.asSingleString() },
                dateTime = value.dateTime,
            ),
        )
    }

    override fun deserialize(decoder: Decoder): CommandValidationResult {
        val serializable = SerializableCommandValidationResult.serializer().deserialize(decoder)
        return CommandValidationResult(
            results = serializable.results.mapKeys { OsCommand.fromSingleString(it.key) },
            dateTime = serializable.dateTime,
        )
    }
}
