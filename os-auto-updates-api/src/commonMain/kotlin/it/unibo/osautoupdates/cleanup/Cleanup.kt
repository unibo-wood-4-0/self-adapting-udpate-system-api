@file:OptIn(ExperimentalJsExport::class)

package it.unibo.osautoupdates.cleanup

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Interface that represents the cleanup to perform on a Deployment.
 */
@Serializable
@SerialName("Cleanup")
sealed interface Cleanup {
    companion object {
        /**
         * @return the list of supported [Cleanup] implementations.
         */
        fun supported(): Set<Cleanup> = setOf(Skip, Full)
    }
}

/**
 * Cleanup that does nothing.
 */
@Serializable
@SerialName(Skip.SERIAL_NAME)
data object Skip : Cleanup {
    override fun toString(): String = SERIAL_NAME

    const val SERIAL_NAME: String = "Skip"
}

/**
 * Cleanup that restore the system to completely clean state, uninstalling all the software from the Deployment.
 */
@Serializable
@SerialName(Full.SERIAL_NAME)
data object Full : Cleanup {
    override fun toString(): String = SERIAL_NAME

    const val SERIAL_NAME: String = "Full"
}
