package it.unibo.osautoupdates.software.version

import it.unibo.osautoupdates.serialization.version.VersionAsStringSerializer
import it.unibo.osautoupdates.software.version.parser.VersionParser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Entity that describes a [Version].
 */

@Serializable(with = VersionAsStringSerializer::class)
@SerialName(Version.SERIAL_NAME)
sealed interface Version : Comparable<Version> {
    /**
     * Utility for the [Version] class.
     */
    companion object {
        /**
         * The [Version] class serial name.
         */
        const val SERIAL_NAME = "Version"

        /**
         * Parses a [String] and returns a [Version] if the string matches one of the [VersionParserModule]s regexes.
         */
        fun String.toVersion(): Version = fromString(this)

        /**
         * Static factory that creates a [Version] from a [String].
         * @param string the [String] to parse.
         * @return a [Version] corresponding to the [String].
         */
        fun fromString(string: String): Version = VersionParser.parse(string)

        /**
         * Static factory that creates a [Version] using the semantic scheme.
         * @param major the major version.
         * @param minor the minor version.
         * @param patch the patch version.
         * @return a [Version] with a [SemanticVersion] type.
         */
        fun semantic(
            major: Int,
            minor: Int,
            patch: Int,
        ): Version = SemanticVersion(major, minor, patch)

        /**
         * Static factory that creates a [Version] specifying a tag.
         */
        fun tag(tag: String): Version = TagVersion(tag)
    }
}
