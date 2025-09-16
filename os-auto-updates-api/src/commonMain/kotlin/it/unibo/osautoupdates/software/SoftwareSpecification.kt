package it.unibo.osautoupdates.software

import it.unibo.osautoupdates.software.VersionedSoftware.Companion.SERIAL_NAME
import it.unibo.osautoupdates.software.version.NotDefined
import it.unibo.osautoupdates.software.version.Version
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An indication of the software's name and version.
 * @param name the name of the software.
 * @param version the version of the software.
 */
@Serializable
@SerialName(SoftwareSpecification.SERIAL_NAME)
data class SoftwareSpecification(
    val name: SoftwareName,
    val version: Version,
) {
    /**
     * String describing the [VersionedSoftware] in terms of its name and version.
     * If [NotDefined] is the version, it is not included in the string.
     * @param separator the separator between the name and the version. Default is "-".
     * @return the name of the [ResolvedSoftware] followed by a [separator] with the version, if present.
     */
    fun asString(separator: String = "-"): String =
        this.name +
            when (this.version) {
                NotDefined -> ""
                else -> "$separator${this.version}"
            }

    /**
     * Utility for the [SoftwareSpecification] class.
     */
    companion object {
        /**
         * The serial name of the [SoftwareSpecification] type, used for serialization.
         */
        const val SERIAL_NAME = "SoftwareSpecification"
    }
}
