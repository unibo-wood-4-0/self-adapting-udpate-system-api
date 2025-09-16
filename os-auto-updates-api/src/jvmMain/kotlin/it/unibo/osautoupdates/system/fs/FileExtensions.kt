

package it.unibo.osautoupdates.system.fs

import arrow.core.raise.Raise
import arrow.core.raise.catch
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.osautoupdates.failure.IOFailure
import it.unibo.osautoupdates.serialization.Formatters
import it.unibo.osautoupdates.serialization.yaml.YamlConverter
import it.unibo.osautoupdates.util.ArrowExtensions.ensure
import it.unibo.osautoupdates.util.ArrowExtensions.raise
import it.unibo.osautoupdates.util.RegexFromRoot
import java.io.File
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.createTempDirectory
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

private val logger = KotlinLogging.logger { }

/**
 * Extensions for the [File] class.
 */
object FileExtensions {
    private const val BYTE_ARRAY_SIZE = 1024

    /**
     * Check if the [File] is deserializable.
     * @return `true` if the file is deserializable, `false` otherwise.
     */
    val File.isDeserializable: Boolean get() = (extension in Formatters.supportedExtensions)

    /**
     * Compute the MD5 hash of the content of the [File].
     */
    fun File.md5(): String =
        MessageDigest
            .getInstance("MD5")
            .apply {
                this@md5.inputStream().use { inputStream ->
                    val buffer = ByteArray(BYTE_ARRAY_SIZE)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        this.update(buffer, 0, bytesRead)
                    }
                }
            }.digest()
            .joinToString("") { "%02x".format(it) }

    /**
     * Use a [RegexFromRoot] to retrieve the first file that matches the regex.
     * @receiver the [RegexFromRoot] to use to retrieve the file.
     */
    fun retrieveFileFromRegex(regexFromRoot: RegexFromRoot): File? =
        Path(regexFromRoot.root)
            .toFile()
            .walkTopDown()
            .firstOrNull {
                val regex = regexFromRoot.build()
                val result = it.absolutePath.matches(regex)
                logger.trace {
                    "Matching PATH=${it.absolutePath} with REGEX=${regex.pattern} resulted in $result"
                }
                result
            }

    /**
     * Deserialize the content of the given [File] to an object of type [T].
     * @param serializer the [KSerializer] to use to deserialize the object. Default is the serializer of [T].
     * @receiver a [IOFailure] occurs if the file can't be deserializable.
     * @return the deserialized object as a [T].
     */
    @OptIn(InternalSerializationApi::class)
    context(_: Raise<IOFailure>)
    inline fun <reified T : Any> File.deserialize(serializer: KSerializer<T> = T::class.serializer()): T {
        ensure(isFile && exists()) {
            IOFailure.NotFound(this.absolutePath)
        }
        ensure(isDeserializable) {
            IOFailure.UnprocessableContent(
                "The file extension of file $absolutePath was not recognized. " +
                    "Please use a valid format, since ($extension) is not supported." +
                    "Allowed extensions include:" +
                    "${Formatters.supportedExtensions.joinToString(", ")}.",
            )
        }
        val originalContent = this.readText(Charsets.UTF_8)
        val contentToParse =
            when {
                extension in Formatters.yamlExtensions -> YamlConverter.convertYamlToJson(originalContent)
                else -> originalContent
            }
        return catch({
            Formatters.json().decodeFromString(serializer, contentToParse)
        }) { exception ->
            raise(
                IOFailure.UnprocessableContent(
                    "The file at path `$this` could not be deserialized: ${exception.message}",
                ),
            )
        }
    }

    /**
     * Convert the [File] to an absolute path.
     * The returned string is cross-platform and can be used in other strings, unlike the [File.absolutePath] property,
     * which is platform-dependent and return "\" on Windows, making it unusable in other strings.
     */
    fun File.toAbsolutePathCrossPlatform(): String = this.toPath().toAbsolutePathCrossPlatform()

    /**
     * @see File.toAbsolutePathCrossPlatform
     */
    fun Path.toAbsolutePathCrossPlatform(): String = this.toAbsolutePath().toString().replace("\\", "/")

    /**
     * Create a temporary folder with the given postfix.
     * @return the absolute path of the created folder.
     */
    fun createTempFolder(postfix: String): String =
        createTempDirectory("os-auto-updates-$postfix").toAbsolutePathCrossPlatform()

    /**
     * Convert a [kotlinx.io.files.Path] to a [File].
     */
    fun kotlinx.io.files.Path.toFile(): File = File(this.toString())
}
