package it.unibo.osautoupdates.util.logs

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import ch.qos.logback.core.OutputStreamAppender
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.oshai.kotlinlogging.Level
import it.unibo.osautoupdates.configuration.LevelString
import it.unibo.osautoupdates.failure.InitializationFailure
import kotlin.collections.contains
import org.slf4j.LoggerFactory

@DslMarker
annotation class LoggingTagMarker

private val logger = logger { }

/**
 * Interface for configuring logging in the application.
 * It is used as a DSL to configure the loggers to use in a submodule
 */
@LoggingTagMarker
interface LoggingConfiguratorDSL {
    /**
     * The name of the logger to use, child of the root logger.
     */
    val loggerName: String

    /**
     * Detach all previous appenders from the root logger.
     */
    fun detachAppender(appenderName: String)

    /**
     * Add a console appender to the root logger with the specified log level.
     * @param level the log level for the console appender.
     */
    fun addConsoleAppender(
        appenderName: String,
        level: Level,
    )

    /**
     * Add a file appender to the root logger with the specified log level and file.
     * @param level the log level for the file appender.
     * @param logFile the file where the logs will be written.
     */
    fun addFileAppender(
        appenderName: String,
        level: Level,
        logFile: LogFile,
    )

    /**
     * Add a console appender to the root logger with the specified log level.
     * This function raises an error if the log level is not supported.
     * @param level the log level for the console appender.
     * @param logFile the file where the logs will be written.
     * @raise the raise context to use for error handling.
     */
    context(raise: Raise<InitializationFailure>)
    fun addFileAppender(
        appenderName: String,
        level: LevelString,
        logFile: LogFile,
    )

    /**
     * Add a console appender to the root logger with the specified log level.
     * This function raises an error if the log level is not supported.
     * @param level the log level for the console appender.
     */
    context(raise: Raise<InitializationFailure>)
    fun addConsoleAppender(
        appenderName: String,
        level: LevelString,
    )

    companion object {
        /**
         * Configure the loggers for the application.
         * @param configurationBlock the configuration block to use to configure the loggers.
         **/
        fun configureLogging(
            name: String,
            configurationBlock: LoggingConfiguratorDSL.() -> Unit,
        ) {
            LoggingConfiguratorDSLImpl(name).configurationBlock()
            logger.debug { "Logs configuration terminated." }
        }
    }

    private class LoggingConfiguratorDSLImpl(
        override val loggerName: String,
    ) : LoggingConfiguratorDSL {
        private val targetLogger = LoggerFactory.getLogger(loggerName) as Logger

        private fun filterThreshold(level: Level) =
            ThresholdFilter().apply {
                setLevel(level.name)
                start()
            }

        private fun OutputStreamAppender<ILoggingEvent>.configureAppenderForLevel(level: Level) {
            val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
            context = loggerContext
            addFilter(filterThreshold(level))
            encoder =
                PatternLayoutEncoder().apply {
                    context = loggerContext
                    pattern = PATTERN.trimIndent()
                    start()
                }
            start()
        }

        override fun detachAppender(appenderName: String) {
            targetLogger.detachAppender(appenderName)
        }

        override fun addConsoleAppender(
            appenderName: String,
            level: Level,
        ) {
            with(ConsoleAppender<ILoggingEvent>()) {
                name = appenderName
                configureAppenderForLevel(level)
                targetLogger.addAppender(this)
                logger.debug { "Console appender added with level ($level)" }
                targetLogger.isAdditive = false
            }
        }

        override fun addFileAppender(
            appenderName: String,
            level: Level,
            logFile: LogFile,
        ) {
            with(FileAppender<ILoggingEvent>()) {
                name = appenderName
                this.file = logFile.absolutePath().toString()
                configureAppenderForLevel(level)
                targetLogger.addAppender(this)
                logger.debug { "File appender added with level ($level) and file (${logFile.absolutePath()})" }
                targetLogger.isAdditive = false
            }
        }

        context(raise: Raise<InitializationFailure>)
        override fun addFileAppender(
            appenderName: String,
            level: LevelString,
            logFile: LogFile,
        ) {
            addFileAppender(appenderName = appenderName, level = raise.toLevelOrRaise(level), logFile = logFile)
        }

        context(raise: Raise<InitializationFailure>)
        override fun addConsoleAppender(
            appenderName: String,
            level: LevelString,
        ) {
            addConsoleAppender(appenderName = appenderName, level = raise.toLevelOrRaise(level))
        }

        private fun Raise<InitializationFailure>.toLevelOrRaise(level: LevelString): Level {
            ensure(level in LogLevel.supported()) {
                InitializationFailure.CheckFailure(
                    "The log level $level is not supported." +
                        "Please specify one of the following: ${LogLevel.supported().joinToString(", ")}",
                )
            }
            return Level.valueOf(level)
        }

        companion object {
            private const val PATTERN = """
            %d{YYYY-MM-dd HH:mm:ss.SSS} %level \(%logger{0}\): %replace([%marker] - ){'\[\] - ',''}%msg%n
        """
        }
    }
}
