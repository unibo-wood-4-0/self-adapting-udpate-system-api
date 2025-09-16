package it.unibo.osautoupdates.software.artifact

import kotlin.jvm.JvmInline

/**
 * Represents a Software Artifact that can be retrieved and used to install a software.
 * @param absolutePath the absolute path of the artifact.
 */
@JvmInline
value class Artifact(
    val absolutePath: String,
)
