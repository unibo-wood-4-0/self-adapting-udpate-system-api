

package it.unibo.osautoupdates.system.fs.composite.folder

import arrow.core.raise.Raise
import arrow.core.raise.catch
import it.unibo.osautoupdates.ds.DS
import it.unibo.osautoupdates.failure.IOFailure
import it.unibo.osautoupdates.failure.IOFailure.FileSystemInteraction
import it.unibo.osautoupdates.software.VersionedSoftware
import it.unibo.osautoupdates.software.artifact.Artifact
import it.unibo.osautoupdates.system.fs.composite.FileSystemNode
import it.unibo.osautoupdates.util.ArrowExtensions.raise
import java.io.File
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer

/**
 * Folder for a specific [DS] for a [VersionedSoftware].
 * Useful to store specific artifact for each [DS].
 */
data class DSFolder<D : DS>(
    private val versionedSoftware: VersionedSoftware,
    private val ds: D,
    override val parent: FileSystemNode?,
) : Folder {
    @OptIn(InternalSerializationApi::class)
    override val folderName: String = ds::class.serializer().descriptor.serialName

    /**
     * Catch an [java.io.IOException] and raise a [IOFailure.FileSystemInteraction] if an error occurs.
     * @param block the block of code to execute.
     */
    context(_: Raise<FileSystemInteraction>)
    private fun <A> catchIOException(block: context(Raise<FileSystemInteraction>) () -> A): A =
        catch({
            block()
        }) { e: Exception ->
            raise(
                FileSystemInteraction(
                    "An error was detected while interacting with the FileSystem: ${e.message}",
                ),
            )
        }

    /**
     * Creates a new file in the folder.
     * @param filename the name of the file to create.
     * @receiver raise a [IOFailure.FileSystemInteraction] if the file can't be created.
     * @return the created [File].
     */
    context(_: Raise<FileSystemInteraction>)
    fun createArtifact(filename: String): Artifact =
        catchIOException {
            toFile().mkdirs()
            val file = child(filename)
            file.createNewFile()
            Artifact(file.absolutePath)
        }

    /**
     * Retrieve the file in the folder if present.
     * @receiver raise a [IOFailure.FileSystemInteraction] if the files can't be retrieved.
     * @return a file if it is found, null otherwise.
     */
    context(_: Raise<FileSystemInteraction>)
    fun artifact(): Artifact? =
        catchIOException {
            toFile()
                .listFiles()
                ?.firstOrNull()
                ?.absolutePath
                ?.let {
                    Artifact(it)
                }
        }
}
