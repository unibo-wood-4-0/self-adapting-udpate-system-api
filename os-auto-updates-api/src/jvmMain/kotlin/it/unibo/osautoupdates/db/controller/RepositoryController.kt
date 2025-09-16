package it.unibo.osautoupdates.db.controller

import arrow.core.Nel
import arrow.core.raise.Raise
import it.unibo.osautoupdates.db.repository.Repository
import it.unibo.osautoupdates.failure.Failure
import it.unibo.osautoupdates.resilience.Resilience

/**
 * A controller for repositories.
 * Represents a storage point for some data.
 * Could be implemented using a database, a file, or any other kind of storage.
 * @param F the type of failure that can occur when interacting with the repository.
 */
interface RepositoryController<F> {
    /**
     * The repository to control.
     */
    val repository: Repository

    /**
     * Check if the repository is available.
     * @receiver a [F] occurs if the repository is not available.
     * @return [Unit] if the repository is available.
     */
    context(_: Raise<F>)
    suspend fun check()

    /**
     * Close the repository.
     */
    suspend fun close()

    /**
     * Utility for [RepositoryController] class.
     */
    companion object {
        /**
         * Make a [RepositoryController] resilient, trying more than once to execute the operation.
         * @param resilience the resilience configuration to use.
         * @return a resilient [RepositoryController].
         */
        fun <F : Failure, NF : Failure> RepositoryController<F>.resilient(
            resilience: Resilience,
            mapError: (F) -> NF,
        ): RepositoryController<Nel<NF>> = ResilientController(this, resilience, mapError)

        /**
         * A controller that wraps another controller and makes it resilient, trying more than once to execute the
         * operation.
         * @param controller the controller to wrap.
         * @param resilience the resilience configuration to use.
         * @param mapError a function to map the error type.
         * @param F the type of failure that can occur when interacting with the repository.
         * @param NF the new type of failure.
         */
        private class ResilientController<F : Failure, NF : Failure>(
            val controller: RepositoryController<F>,
            private val resilience: Resilience,
            private val mapError: (F) -> NF,
        ) : RepositoryController<Nel<NF>> {
            override val repository: Repository = controller.repository

            context(_: Raise<Nel<NF>>)
            override suspend fun check() =
                Resilience.withResilience(resilience, mapError) {
                    controller.check()
                }

            override suspend fun close() = controller.close()
        }
    }
}
