package it.unibo.osautoupdates.db.api

import arrow.core.Nel
import arrow.core.raise.Raise
import it.unibo.osautoupdates.db.controller.RepositoryController
import it.unibo.osautoupdates.db.controller.RepositoryController.Companion.resilient
import it.unibo.osautoupdates.db.filter.RepositoryFilter
import it.unibo.osautoupdates.db.filter.RepositorySorter
import it.unibo.osautoupdates.failure.Failure
import it.unibo.osautoupdates.resilience.Resilience
import it.unibo.osautoupdates.resilience.Resilience.Companion.withResilience
import it.unibo.osautoupdates.util.StringId

class ResilientAPI<F : Failure, NF : Failure, T : Any>(
    private val api: RepositoryAPI<F, T>,
    private val resilience: Resilience,
    private val mapError: (F) -> NF,
) : RepositoryAPI<Nel<NF>, T> {
    override val controller: RepositoryController<Nel<NF>> = api.controller.resilient(resilience, mapError)

    context(_: Raise<Nel<NF>>)
    override suspend fun take(
        limit: Int,
        filter: RepositoryFilter<T>?,
        sorter: RepositorySorter<T>?,
    ): List<T> = execute { api.take(limit, filter, sorter) }

    context(_: Raise<Nel<NF>>)
    private suspend fun <A> execute(block: suspend Raise<F>.() -> A): A = withResilience(resilience, mapError, block)

    context(_: Raise<Nel<NF>>)
    override suspend fun insertOne(element: T): StringId = execute { api.insertOne(element) }

    context(_: Raise<Nel<NF>>)
    override suspend fun takeById(id: StringId): T? = execute { api.takeById(id) }

    context(_: Raise<Nel<NF>>)
    override suspend fun deleteById(id: StringId): T? = execute { api.deleteById(id) }

    context(_: Raise<Nel<NF>>)
    override suspend fun updateById(
        id: StringId,
        element: T,
        upsert: Boolean,
    ): T? = execute { api.updateById(id, element, upsert) }

    companion object {
        /**
         * Make a [RepositoryController] resilient, trying more than once to execute the operation.
         * @param resilience the resilience configuration to use.
         * @return a resilient [RepositoryController].
         */
        fun <F : Failure, NF : Failure, T : Any> RepositoryAPI<F, T>.resilient(
            resilience: Resilience,
            mapError: (F) -> NF,
        ): RepositoryAPI<Nel<NF>, T> = ResilientAPI(this, resilience, mapError)
    }
}
