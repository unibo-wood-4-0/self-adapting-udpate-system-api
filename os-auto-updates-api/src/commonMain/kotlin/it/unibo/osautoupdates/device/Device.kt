package it.unibo.osautoupdates.device

import it.unibo.osautoupdates.device.machine.Machine
import it.unibo.osautoupdates.system.os.Os
import it.unibo.osautoupdates.util.StringId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias DeviceId = StringId

/**
 * Represents a computer in the domain model.
 * Each [Device] may be linked or not linked to a [Machine].
 */
@Serializable
@SerialName(Device.SERIAL_NAME)
sealed interface Device {
    /**
     * The unique identifier of the device.
     */
    val id: DeviceId

    /**
     * The operating system of the device.
     */
    val os: Os

    /**
     * The machine linked to the device, if any.
     */
    val machine: Machine?

    @Serializable
    @SerialName(SERIAL_NAME)
    private data class DeviceImpl(
        @SerialName(ID_NAME)
        val id: DeviceId,
        val os: Os,
        val machine: Machine?,
    )

    /**
     * Utility for [Device] class.
     */
    companion object {
        /**
         * The name of the [Device] type, used for serialization.
         */
        const val SERIAL_NAME = "Device"

        /**
         * The name of the device identifier field, used for serialization.
         */
        const val ID_NAME = "deviceId"
    }
}
