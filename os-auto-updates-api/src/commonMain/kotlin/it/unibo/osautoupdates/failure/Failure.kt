package it.unibo.osautoupdates.failure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Logical failure that can happen in the system.
 * It represents an error that can be handled by the system and is expected to happen in the defined Domain.
 * New-defined failures MUST implement this interface as the higher superclass.
 * In opposition, [Exception] must be thrown when the error is unexpected or cannot be handled by the system,
 * for example when dealing with I/O or network-related errors.
 * This interface is often used inside a [Either] or in a [Raise] context to represent the failure occurring.
 * **See: ** [Arrow.kt - Working with typed errors](https://arrow-kt.io/learn/typed-errors/working-with-typed-errors)
 * TODO: take in consideration sealed keyword removal: https://github.com/Kotlin/kotlinx.serialization/issues/2782
 */
@Serializable
@SerialName("Failure")
sealed interface Failure {
    /**
     * Human-readable message of the failure.
     */
    val message: String
}
