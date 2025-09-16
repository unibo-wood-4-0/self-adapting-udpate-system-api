package it.unibo.osautoupdates.db.api

import arrow.core.Nel
import arrow.core.raise.Raise
import arrow.core.raise.recover
import it.unibo.osautoupdates.db.controller.RepositoryController
import it.unibo.osautoupdates.db.filter.RepositoryFilter
import it.unibo.osautoupdates.db.filter.RepositorySorter
import it.unibo.osautoupdates.db.repository.Repository
import it.unibo.osautoupdates.failure.Failure
import it.unibo.osautoupdates.util.ArrowExtensions.raise
import it.unibo.osautoupdates.util.StringId

fun <F : Failure, NNF : Failure, T : Any> RepositoryAPI<Nel<F>, T>.butOnlyTakeLastFailure(
    mapFailure: (F) -> NNF,
): RepositoryAPI<NNF, T> {
    val previousRepositoryApi = this
    return object : RepositoryAPI<NNF, T> {
        override val controller =
            object : RepositoryController<NNF> {
                override val repository: Repository = previousRepositoryApi.controller.repository

                context(_: Raise<NNF>)
                override suspend fun check() = takeLastFailureAndMapIt { previousRepositoryApi.controller.check() }

                override suspend fun close() = previousRepositoryApi.controller.close()
            }

        context(_: Raise<NNF>)
        override suspend fun insertOne(element: T): StringId =
            takeLastFailureAndMapIt { previousRepositoryApi.insertOne(element) }

        context(_: Raise<NNF>)
        override suspend fun take(
            limit: Int,
            filter: RepositoryFilter<T>?,
            sorter: RepositorySorter<T>?,
        ): List<T> = takeLastFailureAndMapIt { previousRepositoryApi.take(limit, filter, sorter) }

        context(_: Raise<NNF>)
        override suspend fun takeById(id: StringId): T? = takeLastFailureAndMapIt { previousRepositoryApi.takeById(id) }

        context(_: Raise<NNF>)
        override suspend fun updateById(
            id: StringId,
            element: T,
            upsert: Boolean,
        ): T? = takeLastFailureAndMapIt { previousRepositoryApi.updateById(id, element, upsert) }

        context(_: Raise<NNF>)
        override suspend fun deleteById(id: StringId): T? =
            takeLastFailureAndMapIt { previousRepositoryApi.deleteById(id) }

        context(_: Raise<NNF>)
        private suspend fun <A> takeLastFailureAndMapIt(block: suspend Raise<Nel<F>>.() -> A): A =
            recover({ block() }) { raise(mapFailure(it.last())) }
    }
}
