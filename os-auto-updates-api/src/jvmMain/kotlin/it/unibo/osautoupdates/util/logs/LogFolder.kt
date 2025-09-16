package it.unibo.osautoupdates.util.logs

import it.unibo.osautoupdates.util.logs.LogFile.Companion.extensionLogFile
import kotlinx.io.files.Path

/**
 * Represents the directory where logs are stored.
 * This interface provides a way to access the path of the logs directory.
 * @param systemLogFolder the path where the operative system usually stores logs. Can be customized at need.
 * @param folderName the name of the log folder.
 */
abstract class LogFolder(
    folderName: String,
    systemLogFolder: Path,
) {
    val path: Path = Path("$systemLogFolder/$folderName")

    /**
     * Returns the path of the log file with the given name.
     * @param name the name of the log file.
     */
    fun logFile(name: String): LogFile = extensionLogFile(name)
}
