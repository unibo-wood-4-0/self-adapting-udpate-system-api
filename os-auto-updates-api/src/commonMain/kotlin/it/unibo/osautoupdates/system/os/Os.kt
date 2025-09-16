@file:OptIn(ExperimentalJsExport::class)

package it.unibo.osautoupdates.system.os

import kotlin.js.ExperimentalJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An Operating System (OS) of a device.
 * @param family The operating system family.
 * @param edition The operating system edition.
 */
@Serializable
@SerialName(Os.SERIAL_NAME)
data class Os(
    val family: Family,
    val edition: Edition,
) {
    /**
     * Utility for [Os] class.
     * Obtains the current operating system of the device.
     */
    companion object {
        /**
         * @return an [Os] that represent the current operating system of the device.
         */
        fun current(): Os = currentOs()

        /**
         * @return the current operating system family of the device.
         */
        fun currentFamily(): Family = currentOs().family

        /**
         * @return the current operating system edition of the device.
         */
        fun currentEdition(): Edition = currentOs().edition

        /**
         * The name of the [Os] type, used for serialization.
         */
        const val SERIAL_NAME = "Os"
    }
}

/**
 * @return the current operating system.
 */
internal expect fun currentOs(): Os
