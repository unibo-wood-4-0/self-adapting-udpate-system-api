package it.unibo.osautoupdates.system.fs.composite.folder

import it.unibo.osautoupdates.ds.DS
import it.unibo.osautoupdates.software.VersionedSoftware
import it.unibo.osautoupdates.system.fs.composite.FileSystemNode

/**
 * Folder of a specific software with a version in the file system.
 * @param versionedSoftware the software with the version.
 * @param parent the parent of the folder.
 * @see Folder
 */
data class VersionedSoftwareFolder(
    private val versionedSoftware: VersionedSoftware,
    override val parent: FileSystemNode?,
) : Folder {
    override val folderName: String = "${versionedSoftware.name}-${versionedSoftware.version}"

    /**
     * Returns the folder of the [DS] for the [VersionedSoftware].
     * @param ds the [DS].
     * @return the folder of the [DS].
     */
    fun <D : DS> dsFolder(ds: D): DSFolder<D> = DSFolder(versionedSoftware, ds, this)
}
