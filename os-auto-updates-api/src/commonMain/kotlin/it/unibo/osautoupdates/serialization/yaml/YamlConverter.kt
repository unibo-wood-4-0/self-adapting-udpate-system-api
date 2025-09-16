package it.unibo.osautoupdates.serialization.yaml

typealias YamlString = String
typealias JsonString = String

/**
 * Utility object for converting between YAML and JSON formats.
 */
expect object YamlConverter {
    /**
     * Convert a JSON string to a YAML string.
     * @param jsonString the YAML string to convert.
     * @return the JSON string.
     */
    fun convertJsonToYaml(jsonString: JsonString): YamlString

    /**
     * Convert a YAML string to a JSON string.
     * @param yamlString the YAML string to convert.
     * @return the JSON string.
     */
    fun convertYamlToJson(yamlString: YamlString): JsonString
}
