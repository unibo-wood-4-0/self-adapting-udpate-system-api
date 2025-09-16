package it.unibo.osautoupdates.migration.util

import it.unibo.osautoupdates.migration.util.FileCopyOption.Companion.SERIAL_NAME
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Options for copying and moving a file.
 * @param replaceExisting if true, replace an existing file if it exists.
 * @param copyAttributes if true, copy the file attributes.
 * @param atomicMove if true, move the file atomically.
 */
@Serializable
@SerialName(SERIAL_NAME)
data class FileCopyOption(
    val replaceExisting: Boolean,
    val copyAttributes: Boolean,
    val atomicMove: Boolean,
) {
    /**
     * Utility for the [FileCopyOption] class.
     */
    companion object {
        /**
         * Default [FileCopyOption].
         * Replace existing files, copy attributes and move atomically.
         */
        fun default() =
            FileCopyOption(
                replaceExisting = true,
                copyAttributes = true,
                atomicMove = true,
            )

        /**
         * Serialization name of the [FileCopyOption].
         */
        const val SERIAL_NAME = "FileCopyOption"
    }
}
