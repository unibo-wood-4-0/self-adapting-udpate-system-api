package it.unibo.osautoupdates.migration

import it.unibo.osautoupdates.migration.resource.FileResource
import it.unibo.osautoupdates.migration.util.FileCopyOption
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Move a file from a source to a destination.
 * @param source the source file.
 * @param destination the destination file.
 * @param fileCopyOption the options for copying the file.
 */
@Serializable
@SerialName(MoveFile.SERIAL_NAME)
data class MoveFile(
    val source: FileResource,
    val destination: FileResource,
    val fileCopyOption: FileCopyOption = FileCopyOption.default(),
) : Migration {
    override fun reverse(): Migration = MoveFile(destination, source)

    /**
     * Utility for the [MoveFile] class.
     */
    companion object {
        /**
         * Serialization name of the [MoveFile].
         */
        const val SERIAL_NAME = "MoveFile"
    }
}
