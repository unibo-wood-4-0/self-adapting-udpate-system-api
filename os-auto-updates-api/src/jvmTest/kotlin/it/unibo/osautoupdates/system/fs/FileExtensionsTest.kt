package it.unibo.osautoupdates.system.fs
import arrow.core.raise.either
import arrow.core.raise.recover
import arrow.core.right
import io.kotest.assertions.AssertionErrorBuilder.Companion.fail
import io.kotest.core.spec.style.WordSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.serialization.Formatters
import it.unibo.osautoupdates.serialization.yaml.YamlConverter.convertYamlToJson
import it.unibo.osautoupdates.system.fs.FileExtensions.deserialize
import it.unibo.osautoupdates.system.fs.FileExtensions.isDeserializable
import it.unibo.osautoupdates.system.fs.FileExtensions.retrieveFileFromRegex
import it.unibo.osautoupdates.util.RegexFromRoot
import java.io.File
import kotlinx.serialization.builtins.serializer

class FileExtensionsTest :
    WordSpec({

        fun withTempFile(
            extension: String,
            block: (File) -> Unit,
        ) {
            val file = File.createTempFile("temp", extension)
            try {
                block(file)
            } finally {
                file.delete()
            }
        }

        "File" should {
            val string = "test content"
            "be deserializable when it has supported extension and a suitable content" {
                withTempFile(".json") { file ->
                    file.writeText(
                        Formatters.json().encodeToString(string),
                    )
                    file.isDeserializable shouldBe true
                    either {
                        file.deserialize<String>(String.serializer())
                    } shouldBe string.right()
                }
            }
            "return false if it is not serializable" {
                withTempFile(".angelo") { file ->
                    file.isDeserializable shouldBe false
                }
            }
            "fail to deserialize if the content is not suitable" {
                withTempFile(".angelo") { file ->
                    file.writeText("not a json")
                    recover({
                        file.deserialize(String.serializer())
                        fail("Should not reach this point")
                    }) {
                    }
                }
            }
        }

        fun loadFile(name: String): File? =
            this::class.java.classLoader
                .getResource(name)
                ?.toURI()
                ?.let { File(it) }

        fun String.trimAll() = this.replace(" ", "").lines().joinToString("")

        fun testYamlFile(name: String) {
            val yaml = loadFile(name)?.readText(Charsets.UTF_8)
            val json = loadFile("fopl.json")?.readText(Charsets.UTF_8)
            yaml.shouldNotBeNull()
            json.shouldNotBeNull()
            convertYamlToJson(yaml).trimAll() shouldBeEqual json.trimAll()
        }
        "A Yaml" should {
            "be converted into a JSON file" {
                testYamlFile("fopl.yaml")
            }
        }

        "RegexFromRoot" should {
            "be used to retrieve existing files" {
                val tempDir = tempdir(prefix = "os-auto-updates", suffix = "regex-test")
                try {
                    val absolutePath = tempDir.absolutePath
                    (tempDir.listFiles()?.size ?: fail("Could not retrieve $tempDir folder content")) shouldBeEqual 0
                    val child = File.createTempFile("child", "file", tempDir)
                    val regexFromRoot = RegexFromRoot(absolutePath, """[\/?, \\?]child.*file""")
                    val regex = regexFromRoot.build()
                    regex.pattern shouldBeEqual """\Q$absolutePath\E[\/?, \\?]child.*file"""
                    retrieveFileFromRegex(regexFromRoot) shouldBe child
                    retrieveFileFromRegex(RegexFromRoot(absolutePath, "non-existing.*file")) shouldBe null
                } finally {
                    tempDir.deleteRecursively()
                }
            }
        }
    })
