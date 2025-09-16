package it.unibo.osautoupdates.util

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant

class InstantExtensionsTest :
    ShouldSpec({
        should("make the instant file name compatible") {
            setOf(
                "2023-09-15T12:34:56Z" to "2023-09-15T123456Z",
                "2023-09-15T12:34:56.789Z" to "2023-09-15T123456.789Z",
                "1970-01-01T00:00:00Z" to "1970-01-01T000000Z",
                "2023-09-15T12:34:56.123456789Z" to "2023-09-15T123456.123456789Z",
            ).forEach { (input, expected) ->
                checkFileCompatible(input, expected)
            }
        }
    })

private fun checkFileCompatible(
    input: String,
    expected: String,
) {
    Instant.parse(input).toDateTimeStringFileCompatible() shouldBe expected
}
