@file:OptIn(ExperimentalContracts::class, ExperimentalTypeInference::class)

package it.unibo.osautoupdates.util

import arrow.core.Nel
import arrow.core.nel
import arrow.core.raise.Raise
import arrow.core.raise.RaiseAccumulate
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import arrow.core.raise.mapOrAccumulate
import arrow.core.raise.withError
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference

/**
 * Extensions for Arrow types.
 */
object ArrowExtensions {
    /**
     * Shortcut for the [raise] method.
     */
    context(raise: Raise<Error>)
    fun <Error> raise(error: Error): Nothing = raise.raise(error)

    /**
     * Shortcut for the [ensure] method.
     */
    context(raise: Raise<Error>)
    fun <Error> ensure(
        condition: Boolean,
        effectiveRaise: () -> Error,
    ) = raise.ensure(condition) { effectiveRaise() }

    /**
     * Shortcut for the [ensureNotNull] method.
     */
    context(raise: Raise<Error>)
    inline fun <Error, B> ensureNotNull(
        value: B?,
        effectiveRaise: () -> Error,
    ): B {
        contract {
            returns() implies (value != null)
        }
        return raise.ensureNotNull(value) { effectiveRaise() }
    }

    /**
     * Shortcut for the [withError] method.
     */
    context(raise: Raise<Error>)
    inline fun <Error, OtherError, A> withError(
        transform: (OtherError) -> Error,
        @BuilderInference block: Raise<OtherError>.() -> A,
    ): A = raise.withError(transform, block)

    context(raise: Raise<Nel<Error>>)
    suspend fun <Error, A> withNelError(
        @BuilderInference block: suspend Raise<Error>.() -> A,
    ): A =
        withError({ it.nel() }) {
            block()
        }

    /**
     * Shortcut for the [mapOrAccumulate] method.
     */
    context(raise: Raise<Nel<Error>>)
    suspend fun <Error, A, B> mapOrAccumulate(
        iterable: Iterable<A>,
        @BuilderInference transform: RaiseAccumulate<Error>.(A) -> B,
    ): List<B> =
        raise.mapOrAccumulate(iterable) { item ->
            withNelError { transform(item) }
        }
}
