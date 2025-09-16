package it.unibo.osautoupdates.serialization

import arrow.core.nonEmptySetOf
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.osautoupdates.serialization.yaml.YamlJsonConverterFormatter
import it.unibo.osautoupdates.software.Software
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus

private val logger = KotlinLogging.logger { }

/**
 * Container of all the possible formatters that the application uses for serialization.
 */
object Formatters {
    /**
     * Valid extensions for the JSON files.
     */
    val jsonExtensions = nonEmptySetOf("json")

    /**
     * Valid extensions for the YAML files.
     */
    val yamlExtensions = nonEmptySetOf("yaml", "yml")

    /**
     * The set of supported extensions for serialization.
     */
    val supportedExtensions = jsonExtensions + yamlExtensions

    /**
     * Json configurations used when serializing and deserializing classes.
     */
    @OptIn(ExperimentalSerializationApi::class)
    fun json(
        prettyPrint: Boolean = false,
        ignoreUnknownKeys: Boolean = false,
    ): Json =
        Json {
            // SERIALIZATION: Pretty print the JSON
            this.prettyPrint = prettyPrint
            // DESERIALIZATION: Enable the use of additional commas
            allowTrailingComma = true
            encodeDefaults = true
            // DESERIALIZATION: deserialize "null" provided from 3rd party libraries to default value of the property
            coerceInputValues = true
            // DESERIALIZATION: if true, ignore unknown keys, properties not present in the class are skipped
            this.ignoreUnknownKeys = ignoreUnknownKeys
            if (this.ignoreUnknownKeys) {
                logger.warn { "The `ignoreUnknownKeys` option is ENABLED! Unknown keys in the JSON will be SKIPPED!" }
            }
            // SERIALIZATION: Add serializer modules for polymorphic serialization
            serializersModule = serializersModules()
        }

    /**
     * A [StringFormat] that encodes to YAML and decodes from YAML using a [Json] formatter under the hood for
     * conversion.
     *
     */
    fun yaml(ignoreUnknownKeys: Boolean = true): YamlJsonConverterFormatter =
        YamlJsonConverterFormatter(
            json(ignoreUnknownKeys = ignoreUnknownKeys),
        )

    /**
     * All the [StringFormat] being used in the application.
     */
    fun all() = listOf(json(ignoreUnknownKeys = false), yaml(ignoreUnknownKeys = false))

    /**
     * List of all the serializers modules used when serializing and deserializing classes.
     */
    fun serializersModules() =
        listOf(
            OsCommand.serializerModule(),
            Software.serializerModule(),
        ).reduce(SerializersModule::plus)
}
