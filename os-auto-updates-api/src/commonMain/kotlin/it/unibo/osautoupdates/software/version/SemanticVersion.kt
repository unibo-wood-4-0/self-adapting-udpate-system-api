package it.unibo.osautoupdates.software.version

import it.unibo.osautoupdates.software.version.SemanticVersion.Companion.SERIAL_NAME
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 	A type of [Version] that represent a semantic single value.
 * 	@param major number that increments when there are incompatible API changes.
 * 	@param minor number that increments when functionalities are introduced in a backward compatible manner.
 * 	@param patch number that increments when backward compatible bug fixes are introduced.
 */

@Serializable
@SerialName(SERIAL_NAME)
data class SemanticVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
) : Version {
    init {
        require(major >= 0 && minor >= 0 && patch >= 0) {
            "The specified Version ($this) requires all the digits to be positive."
        }
    }

    /**
     * @return the [SemanticVersion] as a formatted string major.minor.patch.
     */
    override fun toString(): String = "$major.$minor.$patch"

    /**
     * Compares the major, minor, and patch components of two [SemanticVersion] instances in this order.
     * @return the difference of the first two diverging values, 0 otherwise.
     * @throws IllegalArgumentException if the specified [Version] is not a [SemanticVersion].
     */
    override fun compareTo(other: Version): Int =
        when (other) {
            is SemanticVersion -> {
                when {
                    major != other.major -> major - other.major
                    minor != other.minor -> minor - other.minor
                    else -> patch - other.patch
                }
            }
            else -> error("The specified version ($other) is not a SemanticSingleVersion.")
        }

    /**
     * Utility for the [SemanticVersion] class.
     */
    companion object {
        /**
         * The regular expression used to validate a [SemanticVersion] string.
         */

        val REGEX: Regex = Regex("^v?\\d+\\.\\d+\\.\\d+\$")

        /**
         * The class serial name used to identify the class when serialized.
         */
        const val SERIAL_NAME = "SemanticVersion"

        /**
         * @return a [SemanticVersion] from a string in the format major.minor.patch.
         */
        fun fromString(string: String): SemanticVersion {
            val versionAsList =
                string
                    .let { if (it.startsWith("v")) it.drop(1) else it }
                    .split(".")
                    .map { it.toInt() }
            require(versionAsList.size == 3) {
                "The Version string must have exactly three parts separated by periods ('MAJOR.MINOR.PATCH')."
            }
            return SemanticVersion(versionAsList[0], versionAsList[1], versionAsList[2])
        }
    }
}
