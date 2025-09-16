package it.unibo.osautoupdates.device.machine.parameters

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a country.
 * @property name The name of the country.
 * @property code The code of the country.
 * @property timezone The timezone of the country.
 */
@Serializable
@SerialName(Country.SERIAL_NAME)
data class Country(
    @SerialName("Name")
    val name: String,
    @SerialName("Code")
    val code: String,
    @SerialName("Timezone")
    val timezone: String,
) {
    /**
     * Utility for [Country] class.
     */
    companion object {
        /**
         * The name of the [Country] type, used for serialization.
         */
        const val SERIAL_NAME = "Country"
    }
}
