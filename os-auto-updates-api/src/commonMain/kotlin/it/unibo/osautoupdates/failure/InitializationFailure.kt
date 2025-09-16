package it.unibo.osautoupdates.failure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [it.unibo.osautoupdates.failure.SoftFailure] that can happen during the initialization of the application.
 */
@Serializable
sealed interface InitializationFailure : SoftFailure {
    /**
     * [InitializationFailure] that can happen during the initialization of the application, for example upon loading
     * the configuration or while checking the system state or the database connection.
     */
    @Serializable
    @SerialName("InitialCheckFailure")
    data class CheckFailure(
        override val message: String,
    ) : InitializationFailure

    /**
     * [InitializationFailure] that happens when an empty Suite is provided to the application.
     */
    @Serializable
    @SerialName("EmptySuite")
    data object EmptySuite : InitializationFailure {
        override val message: String
            get() = "An empty Suite was provided. Please specify at least one Software to deploy."
    }

    /**
     * [MissingConfiguration] that happens when the configuration file is not found.
     */
    @Serializable
    @SerialName("MissingConfiguration")
    data object MissingConfiguration : InitializationFailure {
        override val message: String
            get() =
                "The configuration file could not be found. Please ensure that the configuration file is present " +
                    "in the expected location. An initialization process may be required to create it."
    }
}
