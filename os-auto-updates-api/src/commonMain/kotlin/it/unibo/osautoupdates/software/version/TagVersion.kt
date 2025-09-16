package it.unibo.osautoupdates.software.version

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A type of [Version] that represent a tag (e.g. "latest").
 * @param tag the tag of the [Version].
 */

@Serializable
@SerialName(TagVersion.SERIAL_NAME)
data class TagVersion(
    val tag: String,
) : Version {
    /**
     * Compares the tag components of two [TagVersion]. If the tags are equal,
     * 0 is returned. Otherwise, -1 is always returned.
     */
    override fun compareTo(other: Version): Int =
        when (other) {
            is TagVersion ->
                if (tag == other.tag) {
                    0
                } else {
                    -1
                }
            else -> error("The specified version ($other) is not a TagVersion.")
        }

    /**
     * @return the tag as a string.
     */
    override fun toString(): String = tag

    /**
     * Utility for the [TagVersion] class.
     */
    companion object {
        /**
         * The class serial name.
         */
        const val SERIAL_NAME = "TagVersion"

        /**
         * The regular expression used to validate a [TagVersion] string.
         */

        val REGEX: Regex = Regex(".*")

        /**
         * @return a [SemanticVersion] from a string in the format major.minor.patch.
         */
        fun fromString(string: String): TagVersion = TagVersion(string)
    }
}
