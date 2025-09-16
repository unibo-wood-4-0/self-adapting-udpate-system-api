@file:OptIn(ExperimentalJsExport::class)

package it.unibo.osautoupdates.system.oscommand

import arrow.core.nonEmptySetOf
import it.unibo.osautoupdates.serialization.system.oscommand.DefaultOsCommandSerializer
import it.unibo.osautoupdates.serialization.system.oscommand.OsCommandPolymorphicSerializer
import kotlin.js.ExperimentalJsExport
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule

/**
 * A [OsCommand] is a command that can be executed in the operating system.
 * @property commands the command to execute specified as a list of [String].
 * @property successfulStrategy the strategy to consider the command successful.
 */
@Serializable(with = OsCommandPolymorphicSerializer::class)
@SerialName(OsCommand.SERIAL_NAME)
@Polymorphic
data class OsCommand(
    val commands: List<String>,
    val successfulStrategy: SuccessfulStrategy = SuccessfulStrategy.SuccessfulCodes(nonEmptySetOf(0)),
) {
    /**
     * @return the [commands] as a single string.
     */
    fun asSingleString() = commands.joinToString(" ")

    constructor(vararg commands: String) : this(commands.toList())

    /**
     * Utility for the [OsCommand] class.
     */
    companion object {
        /**
         * The name of the [OsCommand] type, used for serialization.
         */
        const val SERIAL_NAME = "OsCommand"

        /**
         * Static factory that creates a [OsCommand] using the given [command] and splitting it using spaces.
         * @param command the command with arguments to execute.
         */
        fun fromSingleString(
            command: String?,
            successfulCodes: Set<Int> = nonEmptySetOf(0),
        ): OsCommand = OsCommand(command?.split(" ").orEmpty(), SuccessfulStrategy.SuccessfulCodes(successfulCodes))

        /**
         * Static factory that creates an empty [OsCommand].
         * @return an empty [OsCommand].
         */
        fun empty(): OsCommand = OsCommand(emptyList())

        /**
         * Serializer Module for [OsCommand]. Will ALWAYS use the [DefaultOsCommandSerializer] for
         * serialization, while deserialization will be handled by the [OsCommandPolymorphicSerializer], which
         * is annotated in the class declaration.
         */

        fun serializerModule() =
            SerializersModule {
                polymorphic(
                    baseClass = OsCommand::class,
                    actualClass = OsCommand::class,
                    actualSerializer = DefaultOsCommandSerializer,
                )
            }
    }
}
