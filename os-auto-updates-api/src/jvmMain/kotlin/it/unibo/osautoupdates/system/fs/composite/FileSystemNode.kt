package it.unibo.osautoupdates.system.fs.composite

import java.io.File

/**
 * Node in the composite file system hierarchy.
 */
interface FileSystemNode {
    /**
     * The name of the node.
     */
    val folderName: String

    /**
     * The parent of the node.
     */
    val parent: FileSystemNode?

    /**
     * Function that return the file of the folder.
     */
    fun toFile(): File {
        val file = File(absolutePath)
        file.mkdirs()
        return file
    }

    /**
     * The absolute path of the folder.
     */
    val absolutePath: String get() =
        parent?.let { parent ->
            "${parent.absolutePath}/$folderName"
        } ?: folderName
}
