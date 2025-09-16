package it.unibo.osautoupdates.software.version.parser

import it.unibo.osautoupdates.software.version.NotDefined
import it.unibo.osautoupdates.software.version.SemanticVersion
import it.unibo.osautoupdates.software.version.TagVersion
import it.unibo.osautoupdates.software.version.Version
import it.unibo.osautoupdates.software.version.parser.VersionParserModule.Companion.create

/**
 * Entity that can parse a string and create a corresponding [Version].
 */
object VersionParser {
    /**
     * The [VersionParserModule]s that this [VersionParser] uses to parse a string.
     */
    private var modules: List<VersionParserModule> =
        listOf(
            create(SemanticVersion.REGEX) { SemanticVersion.fromString(it) },
            create(NotDefined.REGEX) { NotDefined },
        )

    /**
     * The default [ParseOperation] that is used when no [VersionParserModule] matches the string.
     */
    private val defaultParseOperation: ParseOperation = { TagVersion(it) }

    /**
     * Add a [VersionParserModule] to the [VersionParser].
     * @param module the [VersionParserModule] to add.
     */
    fun add(module: VersionParserModule) {
        modules += module
    }

    /**
     * Add a [Collection] of [VersionParserModule]s to the [VersionParser].
     * @param modules the [Collection] of [VersionParserModule]s to add.
     */
    fun addAll(modules: Collection<VersionParserModule>) {
        modules.forEach { add(it) }
    }

    /**
     * Add the specified [VersionParserModule]s to the [VersionParser].
     * @param modules the [VersionParserModule]s to add.
     */
    fun addAll(vararg modules: VersionParserModule) {
        addAll(modules.toList())
    }

    /**
     * Parses a [String] and returns a [Version] if the string matches one of the [VersionParserModule]s regexes.
     * The [Version] is created using the [VersionParserModule.parseOperation] of the [VersionParserModule] selected.
     * @return a [Version] if the string matches one of the [VersionParserModule]s regexes, otherwise a [TagVersion]
     */
    fun parse(string: String): Version {
        val parseOperation = modules.firstOrNull { it.regex.matches(string) }
        return when (parseOperation) {
            null -> defaultParseOperation(string)
            else -> parseOperation.parseOperation(string)
        }
    }
}
