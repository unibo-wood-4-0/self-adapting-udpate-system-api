package it.unibo.osautoupdates.system.fs.composite.folder

import it.unibo.osautoupdates.software.VersionedSoftware

/**
 * Class that represent the folder where the artifacts are stored.
 */
class SoftwareFolder internal constructor(
    override val folderName: String,
    override val parent: RootFolder,
) : Folder {
    /**
     * Returns the folder of the [VersionedSoftware].
     * @param software the software to store in the folder.
     * @return the folder of the [VersionedSoftware].
     */
    fun versionedSoftwareFolder(software: VersionedSoftware): VersionedSoftwareFolder =
        VersionedSoftwareFolder(software, this)
}
