package it.unibo.osautoupdates.util

import it.unibo.osautoupdates.serialization.Formatters
import kotlinx.serialization.encodeToString

/**
 * Collection of utility functions for strings related operations.
 */
object StringUtility {
    /**
     * Pretty version of the [Any.toString] method.
     * Indent the string with the given [indentWidth].
     * @param indentWidth the width of the indent.
     */
    inline fun <reified T> T.toYamlString() = Formatters.yaml().encodeToString(this)
}
