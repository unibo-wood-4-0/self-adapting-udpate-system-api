

package it.unibo.osautoupdates.software

import arrow.core.Either
import arrow.core.raise.Raise

/**
 * Extension functions for [Software].
 */
object SoftwareExtensions {
    /**
     * Find the first dependency of [S] that satisfies the given [predicate].
     * @param predicate the predicate to satisfy.
     * @return the first dependency that satisfies the [predicate],
     * or `null` if no dependency satisfies the [predicate].
     * @param S the type of the [Software].
     */
    @Suppress("UNCHECKED_CAST")
    fun <S : Software> S.find(predicate: (S) -> Boolean): S? =
        if (predicate(this)) {
            this
        } else {
            dependencies.firstNotNullOfOrNull { (it as S).find(predicate) }
        }

    /**
     * Execute the given [operation] on every dependency of [S] accumulating the results.
     * Also applies the [operation] on [S] itself and return the accumulated result.
     * @param initial the initial value of the accumulator.
     * @param operation the operation to apply on every dependency and on [S] itself.
     * @return the accumulated result of the [operation].
     */
    @Suppress("UNCHECKED_CAST")
    fun <S : Software, R> S.fold(
        initial: R,
        operation: (acc: R, S) -> R,
    ): R {
        val foldResultOnDependencies: R = this.dependencies.fold(initial) { acc, sw -> (sw as S).fold(acc, operation) }
        return operation(foldResultOnDependencies, this)
    }

    /**
     * Execute the given [operation] on every dependency of [S] accumulating the results.
     * Stop the execution if the [operation] returns [Either.Left] at any point.
     * Also applies the [operation] on [S] itself and return the accumulated result.
     * @param initial the initial value of the accumulator.
     * @param operation the operation to apply on every dependency and on [S] itself.
     * @receiver
     * @return the accumulated result of the [operation].
     * @param E the type of the error.
     * @param R the type of the result.
     * @param S the type of the [Software].
     */
    @Suppress("UNCHECKED_CAST")
    context(_: Raise<E>)
    fun <S : Software, E, R> S.foldEither(
        initial: R,
        operation: context(Raise<E>) (acc: R, S) -> R,
    ): R {
        val result = foldEitherOnDependencies(initial, this.dependencies.map { it as S }, emptyList(), operation)
        return operation(result, this)
    }

    @Suppress("UNCHECKED_CAST")
    context(_: Raise<E>)
    private fun <S : Software, E, R> foldEitherOnDependencies(
        acc: R,
        remaining: List<S>,
        done: List<S>,
        operation: context(Raise<E>) (acc: R, S) -> R,
    ): R {
        // if all dependencies have been processed, return the result
        return if (remaining.isEmpty()) {
            acc
        } else {
            val accDependencies =
                foldEitherOnDependencies(
                    acc,
                    remaining.first().dependencies.map { it as S },
                    emptyList(),
                    operation,
                )
            val result = operation(accDependencies, remaining.first())
            foldEitherOnDependencies(result, remaining.drop(1), done + remaining.first(), operation)
        }
    }

    private fun <S : Software> S.flatten(mapFunction: (S) -> S): Set<S> =
        this.fold(emptySet()) { acc, sw ->
            acc + mapFunction(sw)
        }

    /**
     * Flatten the [S] by returning all the [S] in a single set.
     * @return a set containing all the [S].
     */
    fun <S : Software> S.flatten(): Set<S> = this.flatten { it }
}
