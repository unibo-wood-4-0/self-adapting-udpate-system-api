package it.unibo.osautoupdates.validation

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Result of a validation test, containing the date and time when the test was executed.
 */
@Serializable
sealed interface ValidationResult {
    /**
     * The date and time when the validation tests were executed.
     */
    val dateTime: Instant

    /**
     * @return the list of failed validation tests.
     */
    fun failedTests(): List<String>

    /**
     * Checks if the validation tests were successful.
     */
    fun isSuccessful(): Boolean

    /**
     * Checks if the validation tests were failed.
     */
    fun isFailed(): Boolean = !isSuccessful()
}
