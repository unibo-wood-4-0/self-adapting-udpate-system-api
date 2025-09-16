package it.unibo.osautoupdates.failure

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.string.shouldContain

class InitializationFailureTest :
    ShouldSpec({

        // FAILURES
        val checkFailure = InitializationFailure.CheckFailure("oh no")
        val emptySuite = InitializationFailure.EmptySuite

        should("correctly output the message") {
            checkFailure.message shouldContain "oh no"
        }
    })
