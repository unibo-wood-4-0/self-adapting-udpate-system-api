package it.unibo.osautoupdates.resilience

import arrow.core.Nel
import arrow.core.left
import arrow.core.raise.Raise
import arrow.core.raise.recover
import arrow.core.raise.withError
import arrow.core.right
import arrow.core.toNonEmptyListOrNull
import arrow.resilience.Schedule
import arrow.resilience.retryRaise
import it.unibo.osautoupdates.configuration.ResilienceConfiguration
import it.unibo.osautoupdates.util.ArrowExtensions.withError
import kotlin.time.Duration

/**
 * Utility object to handle resilience in the application.
 * @param conf the configuration for the resilience.
 */
class Resilience(
    private val conf: ResilienceConfiguration,
) {
    constructor(
        exponentialBase: Duration,
        exponentialFactor: Double,
        maxRetries: Int,
    ) : this(ResilienceConfiguration(exponentialBase, exponentialFactor, maxRetries))

    /**
     * Retry a block of code with an exponential backoff strategy.
     * @param block the block of code to retry.
     * @receiver raise a [Nel] of [E] if errors occur during the block execution.
     * @return the result of the block execution.
     */
    context(raise: Raise<Nel<E>>)
    suspend fun <E, A> withExponentialBackoff(block: suspend Raise<E>.() -> A): A {
        val schedule =
            Schedule
                .exponential<E>(conf.exponentialBase, conf.exponentialFactor)
                .and(Schedule.recurs(conf.maxRetries.toLong()))
                .jittered()
        val errors = mutableListOf<E>()
        return with(raise) {
            schedule
                .retryRaise {
                    recover({ block().right() }) {
                        errors.add(it)
                        it.left()
                    }.bind()
                }.mapLeft {
                    val errorsNel = errors.toNonEmptyListOrNull()
                    requireNotNull(errorsNel) {
                        """
                        Critical error occurred during Exponential Backoff strategy execution.
                        Failures were detected but could not be retrieved.
                        This is most likely a critical bug in the logic of the application.
                        """.trimIndent()
                    }
                    errorsNel
                }.bind()
        }
    }

    /**
     *  Utility for the Resilience class.
     */
    companion object {
        /**
         * Retry a block of code with the configured resilience.
         * @param resilience the resilience configuration to use.
         * @param block the block of code to retry.
         */
        context(_: Raise<Nel<F>>)
        suspend fun <F, A> withResilience(
            resilience: Resilience,
            block: suspend Raise<F>.() -> A,
        ): A = resilience.withExponentialBackoff { block() }

        /**
         * Retry a block of code with the configured resilience and map the errors.
         * @param resilience the resilience configuration to use.
         * @param withError the function to map the errors.
         * @param block the block of code to retry.
         */
        context(_: Raise<Nel<NF>>)
        suspend fun <F, NF, A> withResilience(
            resilience: Resilience,
            mapError: (F) -> NF,
            block: suspend Raise<F>.() -> A,
        ): A =
            withError({ failures -> failures.map { mapError(it) } }) {
                withResilience(resilience) { block() }
            }
    }
}
