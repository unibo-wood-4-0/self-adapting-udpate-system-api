package it.unibo.osautoupdates.configuration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The configuration for the cached data of the application.
 * @param overrideFetch if true, the system will override the fetch process and use the cached artifact instead if available.
 * The system will fetch the artifact and store it in the cache if set to false,
 * no matter if an artifact is already available.
 */
@Serializable
@SerialName("CacheConfiguration")
data class CacheConfiguration(
    val overrideFetch: Boolean,
)
