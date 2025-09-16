package it.unibo.osautoupdates.system.oscommand

import arrow.core.nonEmptySetOf
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import it.unibo.osautoupdates.serialization.Formatters
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException

val json = Formatters.json()

private fun deserialize(string: String) = json.decodeFromString<OsCommand>(string)

@OptIn(ExperimentalSerializationApi::class)
class OsCommandTest :
    ShouldSpec({

        val defaultCommand = OsCommand("ls", "-l")
        should("create a command with arguments") {
            defaultCommand.commands shouldBe listOf("ls", "-l")
        }
        should("support successful codes") {
            val strategy = defaultCommand.successfulStrategy
            strategy.shouldBeInstanceOf<SuccessfulStrategy.SuccessfulCodes>()
            strategy.successfulCodes shouldBe setOf(0)
            val anotherCommand =
                OsCommand(
                    listOf("ls", "-l"),
                    SuccessfulStrategy.SuccessfulCodes(nonEmptySetOf(0, 6, 7)),
                )
            (anotherCommand.successfulStrategy as SuccessfulStrategy.SuccessfulCodes).successfulCodes shouldBe
                setOf(0, 6, 7)
        }

        should("be serializable and include all the information possible") {
            val command =
                OsCommand(
                    listOf("ls", "-l"),
                    SuccessfulStrategy.SuccessfulCodes(nonEmptySetOf(0, 2)),
                )
            val expected =
                """
                {"commands":["ls","-l"],"successfulStrategy":{"type":"SuccessfulCodes","successfulCodes":[0,2]}}
                """.trimIndent()
            json.encodeToString(command) shouldBe expected
        }
        should("be deserializable from a single string") {
            val string = "\"ls -l\""
            deserialize(string) shouldBe OsCommand("ls", "-l")
        }
        should("be deserialized from an Array") {
            val string = """["ls", "-l"]"""
            deserialize(string) shouldBe OsCommand("ls", "-l")
        }
        val expectedCodes =
            OsCommand(
                listOf("ls", "-l"),
                SuccessfulStrategy.SuccessfulCodes(nonEmptySetOf(0, 2)),
            )
        val expectedSuccess = OsCommand(listOf("ls", "-l"), SuccessfulStrategy.Success(true))

        val stringCommand = """{"command": "ls -l", "successfulCodes": [0, 2] }"""
        val stringSuccess = """{"command": "ls -l", "success": "true" }"""
        val arrayCommand = """{"commands": ["ls", "-l"], "successfulCodes": [0, 2] }"""
        val arraySuccess = """{"commands": ["ls", "-l"], "success": "true" }"""

        should("be deserializable from an object using the commands syntax with array") {
            deserialize(arrayCommand) shouldBe expectedCodes
        }
        should("be deserializable from an object using the command syntax with string") {
            deserialize(stringCommand) shouldBe expectedCodes
        }
        should("be deserializable from an object using the commands syntax with array and success") {
            deserialize(arraySuccess) shouldBe expectedSuccess
        }
        should("be deserializable from an object using the command syntax with string and success") {
            deserialize(stringSuccess) shouldBe expectedSuccess
        }
        should("correctly deserialize a list of differently string") {
            val list = listOf(stringCommand, stringSuccess, arrayCommand, arraySuccess)
            val listExpected = listOf(expectedCodes, expectedSuccess, expectedCodes, expectedSuccess)
            val listString = """[${list.joinToString(",")}]"""
            val deserializedString = json.decodeFromString<List<OsCommand>>(listString)
            val singleDeserialized = list.map { deserialize(it) }
            deserializedString shouldBe listExpected
            singleDeserialized shouldBe listExpected
        }

        should("fail to deserialize from an object with both success and successCodes") {
            val string = """{"commands": ["ls", "-l"], "success": "true", "successfulCodes": [0, 2] }"""
            shouldThrow<SerializationException> { deserialize(string) }
        }
        should("fail to deserialize from an object with both commands and command") {
            val string = """{"commands": ["ls", "-l"], "command": "ls -l", "successfulCodes": [0, 2] }"""
            shouldThrow<SerializationException> { deserialize(string) }
        }
        should("fail to deserialize from an object without commands or command") {
            val string = """{"successfulCodes": [0, 2] }"""
            shouldThrow<MissingFieldException> { deserialize(string) }
        }
    })
