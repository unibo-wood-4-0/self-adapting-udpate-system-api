package it.unibo.osautoupdates.system.fs.composite.folder

import it.unibo.osautoupdates.system.fs.composite.FileSystemNode
import java.io.File

/**
 * Represents a folder in the file system.
 */
interface Folder : FileSystemNode {
    /**
     * Function that returns the file of a child file starting from the root folder.
     * @param childName the name of the child file.
     * @return the file of the child file.
     */
    fun child(childName: String) = File(absolutePathOfChild(childName))

    /**
     * @see child
     * @param childName the name of the child file.
     * @return the absolute path of the child file.
     */
    fun absolutePathOfChild(childName: String) = "$absolutePath${File.separator}$childName"
}
