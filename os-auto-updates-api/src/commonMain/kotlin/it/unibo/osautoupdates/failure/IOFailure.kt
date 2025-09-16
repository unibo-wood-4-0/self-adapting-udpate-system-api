@file:UseSerializers(NonEmptyListSerializer::class, NonEmptySetSerializer::class)

package it.unibo.osautoupdates.failure

import arrow.core.NonEmptyList
import arrow.core.nonEmptyListOf
import arrow.core.serialization.NonEmptyListSerializer
import arrow.core.serialization.NonEmptySetSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

/**
 * [SoftFailure] that indicates a problem with I/O operations.
 */
@Serializable
@SerialName("IOFailure")
sealed interface IOFailure : SoftFailure {
    /**
     * [IOFailure] that indicates an error occurred while interacting with the file system.
     * @property reason the reason why the error occurred.
     */
    @Serializable
    @SerialName("FileSystemInteraction")
    data class FileSystemInteraction(
        val reason: String,
    ) : IOFailure {
        override val message: String = "An error occurred while interacting with the FileSystem: `$reason`"
    }

    /**
     * [IOFailure] that indicates an error occurred involving the Database.
     * @property reason the reason why the error occurred.
     */
    @Serializable
    @SerialName("RepositoryInteraction")
    data class RepositoryInteraction(
        val reason: String,
    ) : IOFailure {
        override val message: String = "An error occurred involving the Database: `$reason`"
    }

    /**
     * [IOFailure] that indicates a resource could not be found. Equivalent to a 404 error.
     * @property resources the resources that could not be found.
     */
    @Serializable
    @SerialName("NotFound")
    data class NotFound(
        val resources: NonEmptyList<String>,
    ) : IOFailure {
        /**
         * Create a [NotFoundEr ror] with the given resource.
         * @param resource the resource that could not be found.
         * @return the [NotFound] with the given resource.
         */
        constructor(resource: String, vararg resources: String) : this(nonEmptyListOf(resource, *resources))

        override val message: String =
            when (resources.size) {
                1 -> "Resource could not be found at the following location: `${resources.first()}`"
                else -> "Resources could not be found at the following locations: `${resources.joinToString(", ")}`"
            }
    }

    /**
     * [IOFailure] that indicates a resource could not be read because of its invalid content.
     * @property reason the reason why the content is invalid.
     */
    @Serializable
    @SerialName("UnprocessableContent")
    data class UnprocessableContent(
        val reason: String,
    ) : IOFailure {
        override val message: String = "Resource could not be read because of its invalid content: `$reason`"
    }
}
