package it.unibo.osautoupdates.serialization.yaml

import com.fasterxml.jackson.databind.ObjectMapper
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

/**
 * Utility object for converting between YAML and JSON formats.
 */
actual object YamlConverter {
    /**
     * Convert a YAML string to a JSON string.
     * @param yamlString the YAML string to convert.
     * @return the JSON string.
     */
    actual fun convertYamlToJson(yamlString: YamlString): JsonString {
        val yamlObj = Yaml().load<Any>(yamlString)
        return ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(yamlObj)
    }

    /**
     * Convert a JSON string to a YAML string.
     * @param jsonString the JSON string to convert.
     * @return the YAML string.
     */
    actual fun convertJsonToYaml(jsonString: JsonString): YamlString {
        val jsonObj = ObjectMapper().readValue(jsonString, Any::class.java)
        val yaml =
            Yaml(
                DumperOptions().apply {
                    defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
                    isPrettyFlow = true
                    indent = 2
                },
            )
        return yaml.dump(jsonObj)
    }
}
