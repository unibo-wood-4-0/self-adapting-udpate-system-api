

package it.unibo.osautoupdates.db.api

import arrow.core.raise.Raise
import it.unibo.osautoupdates.db.controller.RepositoryController
import it.unibo.osautoupdates.db.filter.RepositoryFilter
import it.unibo.osautoupdates.db.filter.RepositorySorter
import it.unibo.osautoupdates.util.StringId

/**
 * An API for repositories.
 * Provides a way to interact with a repository that stores elements of type [T].
 * This class contains the basic operations that can be performed, but can be extended with more specific ones.
 */
interface RepositoryAPI<F, T : Any> {
    /**
     * The repository controller used to interact with the repository.
     */
    val controller: RepositoryController<F>

    /**
     * Insert a new [T] into the repository.
     * @param element the [T] to insert.
     * @receiver a [F] occurs if it was not possible to insert the [T].
     * @return the id of the inserted [T].
     */
    context(_: Raise<F>)
    suspend fun insertOne(element: T): StringId

    /**
     * Find [T]s that satisfy the given [filter] sorted by the given [sorter].
     * @param limit the maximum number of [T]s to find.
     * @param filter the filter to apply to the [T]s.
     * @receiver raise an [F] if it was not possible to retrieve the [T]s.
     * @return the last [T]s in reversed insertion order.
     */
    context(_: Raise<F>)
    suspend fun take(
        limit: Int,
        filter: RepositoryFilter<T>?,
        sorter: RepositorySorter<T>?,
    ): List<T>

    /**
     * Return all the [T]s in the repository.
     * @receiver raise an [F] if it was not possible to retrieve the [T]s.
     */
    context(_: Raise<F>)
    suspend fun takeAll(): List<T> = take(Int.MAX_VALUE, null, null)

    /**
     * Find a [T] by its id.
     * @param id the id of the [T] to find.
     * @receiver raise an [F] if it was not possible to retrieve the [T].
     * @return the [T] with the given id if it exists, null otherwise.
     */
    context(_: Raise<F>)
    suspend fun takeById(id: StringId): T?

    /**
     * Update the [T] with the corresponding id.
     * @param id the id of the [T] to update.
     * @param T the new [T] to replace the old one.
     * @param upsert if true, insert the [T] if it does not exist, otherwise do nothing.
     * @receiver raise an [F] if it was not possible to update the [T].
     * @return the old [T].
     */
    context(_: Raise<F>)
    suspend fun updateById(
        id: StringId,
        element: T,
        upsert: Boolean = false,
    ): T?

    /**
     * Delete a [T] by its id.
     * @param id the id of the [T] to delete.
     * @receiver raise an [F] if it was not possible to delete the [T].
     * @return the deleted [T].
     */
    context(_: Raise<F>)
    suspend fun deleteById(id: StringId): T?
}
