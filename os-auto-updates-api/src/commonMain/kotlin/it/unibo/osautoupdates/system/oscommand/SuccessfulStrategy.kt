@file:OptIn(ExperimentalJsExport::class)

package it.unibo.osautoupdates.system.oscommand

import kotlin.js.ExperimentalJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The strategy to consider a command successful.
 */
@Serializable
@SerialName(SuccessfulStrategy.SERIAL_NAME)
sealed interface SuccessfulStrategy {
    context(output: OsCommandOutput)
    /**
     *  Check whether the output of the command is successful.
     */
    fun test(): Boolean

    context(output: OsCommandOutput)
    /**
     * The reason why the command is successful or unsuccessful based on the strategy outcome.
     */
    fun description(): String

    /**
     * Utility for the [SuccessfulStrategy] interface.
     */
    companion object {
        /**
         * The name of the [SuccessfulStrategy] type, used for serialization.
         */
        const val SERIAL_NAME = "SuccessfulStrategy"
    }

    /**
     * The [OsCommand] is always successful o unsuccessful based on the [success] value.
     * @param success whether the command should always be successful or not.
     */
    @Serializable
    @SerialName(Success.SERIAL_NAME)
    data class Success(
        val success: Boolean,
    ) : SuccessfulStrategy {
        context(_: OsCommandOutput)
        override fun test(): Boolean = true

        context(_: OsCommandOutput)
        override fun description(): String =
            "This command has been configured to always ${
                when (test()) {
                    true -> "be successful."
                    false -> "fail."
                }
            }."

        /**
         * Utility for the [Success] class.
         */
        companion object {
            /**
             * The name of the [Success] type, used for serialization.
             */
            const val SERIAL_NAME = "Success"
        }
    }

    /**
     * The strategy to consider a command successful when at least one of the codes is successful.
     * @param successfulCodes the set of successful codes.
     */
    @Serializable
    @SerialName(SuccessfulCodes.SERIAL_NAME)
    data class SuccessfulCodes(
        val successfulCodes: Set<Int> = setOf(0),
    ) : SuccessfulStrategy {
        init {
            require(successfulCodes.isNotEmpty()) {
                "Every OsCommand must have at least one successful code. At least [0] should have been included."
            }
        }

        context(output: OsCommandOutput)
        override fun test(): Boolean = output.code in successfulCodes

        context(_: OsCommandOutput)
        override fun description(): String =
            "Considering the provided successful codes " +
                successfulCodes.joinToString(prefix = "[", postfix = "]", separator = ",") +
                " the outcome is treated as " +
                when (test()) {
                    true -> ""
                    false -> "NOT "
                } + "successful."

        /**
         * Utility for the [SuccessfulCodes] class.
         */
        companion object {
            /**
             * The name of the [SuccessfulCodes] type, used for serialization.
             */
            const val SERIAL_NAME = "SuccessfulCodes"
        }
    }
}
