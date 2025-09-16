package it.unibo.osautoupdates.util

/**
 * Represent a way of building a [Regex] that retrieve the first available file on a file system.
 * @param root the root path to use to build the regex.
 * @param matcher the regex to use to match the file.
 */
data class RegexFromRoot(
    val root: String,
    val matcher: String,
) {
    /**
     * Build a regex using the root and the matcher.
     * The pattern is escaped using `\Q` and `\E` to avoid any special character in the root.
     * Example: using "root" as root and "matcher" as matcher will produce the regex "\Qroot\Ematcher".
     * @return the regex built using the root and the matcher.
     */
    fun build(): Regex = Regex("""\Q$root\E$matcher""")
}
