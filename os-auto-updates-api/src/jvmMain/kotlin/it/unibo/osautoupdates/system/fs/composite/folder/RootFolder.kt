package it.unibo.osautoupdates.system.fs.composite.folder

import it.unibo.osautoupdates.system.fs.composite.FileSystemNode
import java.io.File

/**
 * Root folder in the file system to store application data.
 * @param absolutePath the absolute path of the folder
 */
data class RootFolder(
    override val absolutePath: String,
) : Folder {
    override val folderName: String = File(absolutePath).name

    override val parent: FileSystemNode? = null

    /**
     * Returns the folder where the artifacts are stored.
     */
    fun softwareFolder(): SoftwareFolder = SoftwareFolder(ARTIFACTS_FOLDER_NAME, this)

    /**
     * Utility for [RootFolder] class.
     */
    companion object {
        /**
         * The name of the folder where the artifacts are stored.
         */
        private const val ARTIFACTS_FOLDER_NAME = "software"

        /**
         * The name of the folder where the logs are stored.
         */
        private const val LOGS_FOLDER_NAME = "logs"
    }
}
