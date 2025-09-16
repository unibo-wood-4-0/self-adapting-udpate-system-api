package it.unibo.osautoupdates.device

import it.unibo.osautoupdates.software.SoftwareSpecification
import it.unibo.osautoupdates.system.os.Os
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Typealias for [DeviceSpecificationInfo].
 */
typealias DSI = DeviceSpecificationInfo

/**
 * Information about the device state.
 * @param os the operating system of the device.
 * @param installedSoftware the list of the installed software on the device, specified as [SoftwareSpecification].
 */
@Serializable
@SerialName(DeviceSpecificationInfo.SERIAL_NAME)
data class DeviceSpecificationInfo(
    val deviceId: DeviceId,
    val os: Os,
    val installedSoftware: List<SoftwareSpecification>,
) {
    /**
     * Utility for [DeviceSpecificationInfo] class.
     */
    companion object {
        /**
         * The name of the [DSI] type, used for serialization.
         */
        const val SERIAL_NAME = "DSI"
    }
}
