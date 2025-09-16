package it.unibo.osautoupdates.failure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [Failure] that happened in the domain layer, in a macro step of the update process.
 * It is a logical failure that can be handled by the system and can be treated in some way.
 * Different from [CriticalFailure] which indicates a failure that cannot be handled.
 */
@Serializable
@SerialName("SoftFailure")
sealed interface SoftFailure : Failure
