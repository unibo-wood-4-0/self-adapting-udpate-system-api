package it.unibo.osautoupdates.db.filter

import arrow.core.nel
import com.mongodb.client.model.Sorts
import it.unibo.osautoupdates.deployment.suite.SuiteDeployment
import org.bson.conversions.Bson

/**
 * Provides different ways to sort the results of a repository.
 * @param T the type of the elements to sort.
 */
interface RepositorySorter<T : Any> {
    /**
     * Whether the sorting is ascending or descending.
     */
    val ascending: Boolean

    /**
     * The fields to use to sort the elements.
     */
    fun mongoFields(): List<String>

    /**
     * The selectors to use to sort the elements.
     * @return a list of selectors.
     */
    fun selectors(): List<(T) -> Comparable<*>>

    /**
     * Convert the filter to a MongoDB filter.
     */
    fun asMongoSorts(): Bson =
        if (ascending) {
            Sorts.ascending(mongoFields())
        } else {
            Sorts.descending(mongoFields())
        }

    /**
     * Sort the elements of an iterable using the given selector.
     * @return a function that sorts the elements of an iterable.
     */
    fun asFunction(): (List<T>) -> List<T> =
        { list ->
            val ordered = list.sortedWith(compareBy(*selectors().toTypedArray()))
            if (ascending) {
                ordered
            } else {
                ordered.reversed()
            }
        }

    /**
     * Utility for [RepositorySorter] class.
     */
    companion object {
        /**
         * Create a sorter for [SuiteDeployment] elements.
         * @param ascending whether the sorting is ascending or descending.
         */
        fun dateTimeSorter(ascending: Boolean): RepositorySorter<SuiteDeployment> =
            object : RepositorySorter<SuiteDeployment> {
                override val ascending: Boolean = ascending

                override fun mongoFields(): List<String> = SuiteDeployment::datetime.name.nel()

                override fun selectors(): List<(SuiteDeployment) -> Comparable<*>> = SuiteDeployment::datetime.nel()
            }
    }
}
