package it.unibo.osautoupdates.label.implementations

import it.unibo.osautoupdates.device.DSI
import it.unibo.osautoupdates.device.DeviceId
import it.unibo.osautoupdates.label.AbstractLabel
import it.unibo.osautoupdates.label.LabelExpression

/**
 * A label representing the unique identifier of a device.
 * This label can be used to identify a specific device in the system.
 */
class DeviceIdLabel private constructor(
    override val value: DeviceId,
) : AbstractLabel<String, DeviceId>("deviceId", value) {
    override fun evaluate(dsi: DSI): Boolean = dsi.deviceId == value

    companion object {
        /**
         * Factory method for creating a [DeviceIdLabel].
         * @param value the unique identifier of the device to be represented by the label.
         * @return a new instance of [DeviceIdLabel].
         */
        fun deviceId(value: DeviceId): LabelExpression = DeviceIdLabel(value)
    }
}
