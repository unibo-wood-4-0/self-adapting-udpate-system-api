package it.unibo.osautoupdates.suite

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.software.ResolvedSoftware.Companion.resolvedSoftware
import it.unibo.osautoupdates.suite.Suite.Companion.resolvedSuite

class SuiteBuilderTest :
    ShouldSpec({
        val sw1 = resolvedSoftware("sw1", "1.0.0")
        val sw2 = resolvedSoftware("sw2", "1.0.0")
        val suite =
            resolvedSuite {
                +sw1
                +sw2
            }
        should("create a suite with two software") {
            with(suite) {
                size shouldBe 2
                shouldContain(sw1)
                shouldContain(sw2)
            }
        }
    })
