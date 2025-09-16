package it.unibo.osautoupdates.configuration

import it.unibo.osautoupdates.db.repository.Repository
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The logging level expressed as a string.
 */
typealias LevelString = String

/**
 * The global configuration of the system.
 * @param cache the configuration for the artifact caching process.
 * @param rollback the configuration for the rollback process.
 * @param downgrade the configuration for the downgrade process.
 * @param repository the repository where the system will store the deployment results.
 */
@Serializable
@SerialName(Configuration.SERIAL_NAME)
data class Configuration(
    val cache: CacheConfiguration,
    val downgrade: DowngradeConfiguration,
    val repository: Repository,
    val rollback: RollbackConfiguration,
) {
    companion object {
        const val SERIAL_NAME = "Configuration"
    }
}
