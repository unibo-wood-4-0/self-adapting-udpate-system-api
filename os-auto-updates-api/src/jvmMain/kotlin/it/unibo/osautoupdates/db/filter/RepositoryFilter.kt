package it.unibo.osautoupdates.db.filter

import arrow.core.Nel
import arrow.core.raise.Raise
import org.bson.conversions.Bson

/**
 * Provides different ways to filter the results of a repository.
 */
interface RepositoryFilter<in A> {
    /**
     * Convert the filter to a MongoDB filter.
     */
    fun asMongo(): Bson

    /**
     * Raise a failure if the filter not valid, otherwise return the filter.
     */
    context(_: Raise<Nel<String>>)
    fun asRaise(): (A) -> Unit

    /**
     * Convert the filter to a predicate.
     */
    fun asPredicate(): (A) -> Boolean
}
