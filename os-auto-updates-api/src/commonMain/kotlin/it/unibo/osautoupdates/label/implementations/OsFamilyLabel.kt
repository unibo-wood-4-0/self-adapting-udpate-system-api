package it.unibo.osautoupdates.label.implementations

import it.unibo.osautoupdates.device.DSI
import it.unibo.osautoupdates.label.AbstractLabel
import it.unibo.osautoupdates.label.LabelExpression
import it.unibo.osautoupdates.system.os.Family

/**
 * A label representing the operating system family of a device.
 */
class OsFamilyLabel private constructor(
    override val value: Family,
) : AbstractLabel<String, Family>("os.family", value) {
    override fun evaluate(dsi: DSI): Boolean = dsi.os.family == value

    /**
     * Utility for the [OsFamilyLabel] companion object.
     */
    companion object {
        /**
         * Factory method for creating an [OsFamilyLabel].
         * @param value the operating system family to be represented by the label.
         */
        fun osFamily(value: Family): LabelExpression = OsFamilyLabel(value)
    }
}
