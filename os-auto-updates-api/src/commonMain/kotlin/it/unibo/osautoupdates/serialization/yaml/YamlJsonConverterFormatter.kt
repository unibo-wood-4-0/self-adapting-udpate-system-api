package it.unibo.osautoupdates.serialization.yaml

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

/**
 * A [StringFormat] that encode to YAML and decode from YAML using a [Json] formatter under the hood for conversion.
 */
class YamlJsonConverterFormatter internal constructor(
    val jsonFormatter: Json,
) : StringFormat {
    override val serializersModule: SerializersModule = jsonFormatter.serializersModule

    override fun <T> encodeToString(
        serializer: SerializationStrategy<T>,
        value: T,
    ): String =
        YamlConverter.convertJsonToYaml(
            jsonFormatter.encodeToString(serializer, value),
        )

    override fun <T> decodeFromString(
        deserializer: DeserializationStrategy<T>,
        string: String,
    ): T =
        jsonFormatter.decodeFromString(
            deserializer,
            YamlConverter.convertYamlToJson(string),
        )
}
