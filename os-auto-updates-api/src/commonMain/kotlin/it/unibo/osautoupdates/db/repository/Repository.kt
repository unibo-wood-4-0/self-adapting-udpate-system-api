package it.unibo.osautoupdates.db.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A [Repository] is a repository that stores the results of the deployment of a software.
 */
@Serializable
@SerialName(Repository.SERIAL_NAME)
sealed interface Repository {
    /**
     *  Repository companion object.
     */
    companion object {
        const val SERIAL_NAME = "Repository"
    }

    /***
     * Credentials to access the repository.
     * @param username the username to access the repository.
     * @param password the password to access the repository.
     */
    @Serializable
    @SerialName("Credentials")
    data class Credentials(
        val username: String?,
        val password: String?,
    ) {
        override fun toString(): String = "Credentials=*hidden*)"

        /**
         * Utility for the [Credentials] class.
         */
        companion object {
            /**
             * Empty credentials.
             */
            val empty = Credentials(null, null)
        }
    }
}
