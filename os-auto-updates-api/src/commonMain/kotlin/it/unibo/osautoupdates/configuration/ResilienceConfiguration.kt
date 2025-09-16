package it.unibo.osautoupdates.configuration

import it.unibo.osautoupdates.resilience.Resilience
import kotlin.time.Duration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The configuration for the resilience of the system.
 * @param exponentialBase the base of the exponential backoff.
 * @param exponentialFactor the factor to multiply the base at each retry.
 * @param maxRetries the maximum number of retries.
 */
@Serializable
@SerialName("resilience")
data class ResilienceConfiguration(
    val exponentialBase: Duration,
    val exponentialFactor: Double,
    val maxRetries: Int,
) {
    companion object {
        /**
         * Get the default resilience, with a base of 0.25 seconds, a factor of 2.0, and a maximum of 3 retries.
         */
        fun default(): ResilienceConfiguration = ResilienceConfiguration(Duration.parse("0.25s"), 2.0, 3)
    }
}
