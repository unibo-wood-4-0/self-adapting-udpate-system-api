package extensions

/**
 * Extension for the jvm-application-convention.
 * @param mainClass the main class name.
 */
open class JvmApplicationExtension(
    var mainClass: String? = null,
) {
    companion object {
        /**
         * The name of the extension.
         */
        const val EXTENSION_NAME = "jvmApplication"
    }
}
