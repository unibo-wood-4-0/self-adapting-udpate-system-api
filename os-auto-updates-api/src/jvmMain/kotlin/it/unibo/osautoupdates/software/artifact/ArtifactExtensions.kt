package it.unibo.osautoupdates.software.artifact

import java.io.File

/**
 * Extension function to convert an [Artifact] to a [File].
 * @return the [File] of the [Artifact].
 */
fun Artifact.asFile(): File = File(this.absolutePath)
