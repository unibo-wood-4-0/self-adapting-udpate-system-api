package it.unibo.osautoupdates.system.os

/**
 * @return the current operating system.
 */
actual fun currentOs(): Os {
    val os = System.getProperty("os.name")
    val osLowercase = os.lowercase()
    return when {
        osLowercase.contains("win") -> Os(Windows, Edition(os))
        listOf("nix", "nux", "aix").any { osLowercase.contains(it) } -> Os(Linux, Edition(os))
        else ->
            error(
                "The operating system running the application is not supported.",
            )
    }
}

/**
 * @return the current operating system family.
 */
fun currentOsFamily(): Family = currentOs().family
