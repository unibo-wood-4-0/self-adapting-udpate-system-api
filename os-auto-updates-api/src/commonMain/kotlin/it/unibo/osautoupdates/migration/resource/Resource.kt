package it.unibo.osautoupdates.migration.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A resource that can be manipulated by a [Migration].
 */
@Serializable
@SerialName(Resource.SERIAL_NAME)
sealed interface Resource {
    /**
     * Utility for the [Resource] class.
     */
    companion object {
        /**
         * Serialization name of the [Resource].
         */
        const val SERIAL_NAME = "Resource"
    }
}

/**
 * A file resource.
 * @param pathName the path of the file.
 */
@Serializable
@SerialName(FileResource.SERIAL_NAME)
data class FileResource(
    val pathName: String,
) : Resource {
    /**
     * Utility for the [FileResource] class.
     */
    companion object {
        /**
         * Serialization name of the [FileResource].
         */
        const val SERIAL_NAME = "File"
    }
}
