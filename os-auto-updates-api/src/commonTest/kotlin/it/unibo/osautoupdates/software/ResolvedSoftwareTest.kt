package it.unibo.osautoupdates.software

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.ds.DoesNothingDS
import it.unibo.osautoupdates.software.ResolvedSoftware.Companion.resolvedSoftware
import it.unibo.osautoupdates.system.oscommand.OsCommand

class ResolvedSoftwareTest :
    ShouldSpec({
        val validationCommands =
            listOf("test", "test2", "test3", "test4")
                .map { OsCommand(it) }
        val sw =
            resolvedSoftware("test", "1.0.0") {
                dss { +DoesNothingDS }
                validationTests {
                    validationCommands.forEach {
                        +it
                    }
                }
                dependencies {
                    +resolvedSoftware("dep1", "1.0.0") {
                        dss { +DoesNothingDS }
                        validationTests { +OsCommand("test") }
                    }
                }
            }

        should("create a software") {
            with(sw) {
                name shouldBe "test"
                validationTests shouldBe validationCommands
                dependencies.size shouldBe 1
                deploymentStrategies.size shouldBe 1
                deploymentStrategies.first() shouldBe DoesNothingDS
                sw.dependencies.first().apply {
                    name shouldBe "dep1"
                    validationTests shouldBe listOf(OsCommand("test"))
                    dependencies.size shouldBe 0
                    version.toString() shouldBe "1.0.0"
                }
            }
        }
    })
