package it.unibo.osautoupdates.util.logs

import kotlinx.io.files.Path

/**
 * Represents a log file in the system.
 * Provides methods to access the file name and its absolute path.
 */
interface LogFile {
    /**
     * Returns the path of the log file.
     */
    fun name(): String

    /**
     * Returns the absolute path of the log file.
     */
    fun absolutePath(): Path

    private class LogFileImpl(
        private val fileName: String,
        private val logFolder: LogFolder,
    ) : LogFile {
        override fun name(): String = fileName

        override fun absolutePath(): Path = Path("${logFolder.path}/$fileName")
    }

    companion object {
        /**
         * Extension function to obtain a [LogFile] from a [LogFolder] and a file name.
         */
        internal fun LogFolder.extensionLogFile(fileName: String): LogFile = LogFileImpl(fileName, this)
    }
}
