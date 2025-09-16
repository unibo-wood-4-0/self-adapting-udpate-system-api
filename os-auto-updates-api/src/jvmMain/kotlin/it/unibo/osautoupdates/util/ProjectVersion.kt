package it.unibo.osautoupdates.util

/**
 * This object is used to get the project version from the package information.
 */
object ProjectVersion {
    /**
     * This function returns the project version from the package information.
     * It uses the implementation version of the package to get the version.
     * If the implementation version is not found, it returns "unknown".
     *
     * @return The project version as a string.
     */
    fun getProjectVersion(): String {
        val clazz = {}.javaClass
        val implementationVersion = clazz.`package`?.implementationVersion
        return implementationVersion ?: "unknown"
    }
}
