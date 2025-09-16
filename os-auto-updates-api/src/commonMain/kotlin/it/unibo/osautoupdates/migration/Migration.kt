package it.unibo.osautoupdates.migration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A migration that can be applied to [Resource]s.
 * Migration are reversible and non-atomic operations.
 */
@Serializable
@SerialName(Migration.SERIAL_NAME)
sealed interface Migration {
    /**
     * @return the reverse migration.
     */
    fun reverse(): Migration

    /**
     * Utility for the [Migration] class.
     */
    companion object {
        /**
         * Serialization name of the [Migration].
         */
        const val SERIAL_NAME = "Migration"
    }
}
