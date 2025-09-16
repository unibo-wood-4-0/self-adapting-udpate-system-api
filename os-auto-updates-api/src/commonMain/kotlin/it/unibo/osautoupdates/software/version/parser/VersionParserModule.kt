package it.unibo.osautoupdates.software.version.parser

import it.unibo.osautoupdates.software.version.Version

typealias ParseOperation = (String) -> Version

/**
 * Entity that describes a module of a [VersionParser].
 * Usually each [Version] subclass has its own [VersionParserModule].
 * @property regex the [Regex] that matches the [Version]s of this module.
 * @property parseOperation the operation that parses a [String] into a [Version].
 */
interface VersionParserModule {
    val regex: Regex
    val parseOperation: ParseOperation

    /**
     * Utility for the [VersionParserModule] class.
     */
    companion object {
        /**
         * Static factory that creates a [VersionParserModule] using the specified [Regex] and [ParseOperation].
         */
        fun create(
            regex: Regex,
            parseOperation: ParseOperation,
        ): VersionParserModule = VersionParserModuleImpl(regex, parseOperation)
    }
}

/**
 * Basic implementation of a [VersionParserModule].
 */
internal data class VersionParserModuleImpl(
    override val regex: Regex,
    override val parseOperation: ParseOperation,
) : VersionParserModule
