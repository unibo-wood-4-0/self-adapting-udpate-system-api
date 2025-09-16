import java.nio.charset.Charset
import org.gradle.api.Project

/**
 * A semantic version defined using git commits to define major, minor and patch versions.
 * @param version the version string.
 */
@JvmInline
value class GitVersion(
    private val version: String,
) {
    override fun toString(): String = version

    /**
     * Returns the [gitVersion] without the leading 'v' character, making it compatible with Windows.
     */
    fun asWindowsCompliant() = GitVersion(toString().removePrefix("v"))

    companion object {
        /**
         * Returns the [GitVersion] of the project.
         */
        fun Project.gitVersion(): GitVersion {
            val text =
                providers
                    .exec {
                        commandLine("git", "describe", "--tags", "--abbrev=0")
                    }.standardOutput.asText
                    .get()
                    .trim()
            val stringVersion =
                try {
                    rootProject.projectDir
                        .listFiles()
                        ?.first { file -> file.name == ".next-version" }
                        ?.readText(Charset.defaultCharset())
                        ?.trim()
                        ?.also {
                            logger.debug("Next release version is: $it!!")
                        } ?: throw NoSuchElementException("No next version")
                } catch (_: NoSuchElementException) {
                    logger.debug("No next release expected, using current version")
                    text
                }
            return GitVersion(stringVersion)
        }
    }
}
