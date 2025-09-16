package it.unibo.osautoupdates.device.machine

import it.unibo.osautoupdates.device.machine.parameters.Country
import it.unibo.osautoupdates.util.StringId
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias Activations = String
typealias Entitlements = String
typealias ArchType = String
typealias Brand = String
typealias Device = String
typealias MapView = String
typealias Prepared = String
typealias Platform = String
typealias TicketManager = String

/**
 * Represents a machine.
 * @property alias The alias of the machine.
 * @property archType The architecture type of the machine.
 * @property brand The brand of the machine.
 * @property branchId The branch identifier of the machine.
 * @property country The country of the machine.
 * @property createdAt The creation date of the machine.
 * @property dealerId The dealer identifier of the machine.
 * @property freeTrialReady The free trial readiness of the machine.
 * @property goal The goal of the machine.
 * @property id The identifier of the machine.
 * @property serial The serial of the machine.
 * @property family The family of the machine.
 * @property manufacturer The manufacturer of the machine.
 * @property model The model of the machine.
 * @property organizationCode The organization code of the machine.
 * @property organizationCountry The organization country of the machine.
 * @property sdhMachineId The SDH machine identifier of the machine.
 * @property updatedAt The update date of the machine.
 * @property vendorId The vendor identifier of the machine.
 * @property installationDate The installation date of the machine.
 * @property manufacturingDate The manufacturing date of the machine.
 * @property shipmentDate The shipment date of the machine.
 */
@Serializable
@SerialName(Machine.SERIAL_NAME)
data class Machine(
    // val activations: Activations
    // val entitlements: Entitlements
    @SerialName("Alias")
    val alias: String? = null,
    @SerialName("ArchType")
    val archType: ArchType,
    @SerialName("Brand")
    val brand: Brand? = null,
    @SerialName("Branch_Id")
    val branchId: StringId? = null,
    @SerialName("Country")
    @Contextual
    val country: Country,
    @SerialName("Created_at")
    val createdAt: Instant,
    @SerialName("Dealer_Id")
    val dealerId: StringId? = null,
    // val device: Device,
    @SerialName("FreeTrialReady")
    val freeTrialReady: Boolean,
    @SerialName("Goal")
    val goal: Int,
    @SerialName("Id")
    val id: StringId, // UUID
    @SerialName("Machine_Serial")
    val serial: String,
    @SerialName("MachineFamily")
    val family: String,
    @SerialName("Manufacturer")
    val manufacturer: String,
    // val mapView: MapView
    @SerialName("Model_Name")
    val model: String,
    @SerialName("OrganizationCode")
    val organizationCode: String? = null,
    @SerialName("OrgCountry")
    val organizationCountry: String,
    // val prepared: Prepared
    // val properties: Properties
    // val platform: Platform
    @SerialName("SdhMachineId")
    val sdhMachineId: StringId? = null,
    // val tickerManager: TicketManager
    @SerialName("Updated_at")
    val updatedAt: Instant,
    @SerialName("Vendor_Id")
    val vendorId: StringId? = null,
    @SerialName("InstallationDate")
    val installationDate: Instant? = null,
    @SerialName("ManufacturingDate")
    val manufacturingDate: Instant? = null,
    @SerialName("ShipmentDate")
    val shipmentDate: Instant? = null,
    // val os: Os?,
) {
    /**
     * Utility for [Machine] class.
     */
    companion object {
        /**
         * The name of the [Machine] type, used for serialization.
         */
        const val SERIAL_NAME = "Machine"
    }
}
