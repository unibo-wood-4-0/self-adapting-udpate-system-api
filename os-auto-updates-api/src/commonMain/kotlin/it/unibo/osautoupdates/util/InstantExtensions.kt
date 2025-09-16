package it.unibo.osautoupdates.util

import kotlinx.datetime.Instant

/**
 * Extension function to convert an [Instant] to a string that is compatible with file naming.
 * This replaces the colon characters with an empty string to avoid issues in file names.
 * @return A string representation of the [Instant] suitable for file names.
 */
fun Instant.toDateTimeStringFileCompatible(): String = this.toString().replace(":", "")
