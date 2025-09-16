package it.unibo.osautoupdates.configuration

import io.github.oshai.kotlinlogging.Level
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The configuration for the logging process.
 * @param consoleLevel the [Level] of the console logger.
 * @param fileLevel the [Level] of the file logger.
 */
@Serializable
@SerialName("logging")
data class LogConfiguration(
    val consoleLevel: LevelString,
    val fileLevel: LevelString,
) {
    companion object {
        /**
         * The default logging configuration.
         * Console level: INFO
         * File level: DEBUG
         */
        fun default() = LogConfiguration(consoleLevel = Level.INFO.name, fileLevel = Level.DEBUG.name)

        /**
         * A logging configuration with all loggers turned off.
         */
        fun off() = LogConfiguration(consoleLevel = Level.OFF.name, fileLevel = Level.OFF.name)
    }
}
