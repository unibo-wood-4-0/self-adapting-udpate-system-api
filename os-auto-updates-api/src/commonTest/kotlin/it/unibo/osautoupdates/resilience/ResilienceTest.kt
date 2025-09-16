package it.unibo.osautoupdates.resilience

import arrow.core.Nel
import arrow.core.raise.Raise
import arrow.core.raise.recover
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import kotlin.time.Duration.Companion.milliseconds

context(_: Raise<Nel<String>>)
suspend fun Resilience.testResilience(initialCounter: Int): String {
    var counter = initialCounter
    return withExponentialBackoff {
        if (counter > 0) {
            counter--
            raise("Error")
        } else {
            "Success"
        }
    }
}

class ResilienceTest :
    ShouldSpec({
        val resilience =
            Resilience(
                exponentialBase = 0.25.milliseconds,
                exponentialFactor = 2.0,
                maxRetries = 3,
            )
        should("retry a block of code with an exponential backoff strategy") {
            (0..10).forEach { initialCounter ->
                recover({
                    val result =
                        resilience.testResilience(initialCounter = initialCounter)
                    result shouldBe "Success"
                    initialCounter shouldBeLessThan 4
                }) {
                    initialCounter shouldBeGreaterThanOrEqual 4
                }
            }
        }
    })
