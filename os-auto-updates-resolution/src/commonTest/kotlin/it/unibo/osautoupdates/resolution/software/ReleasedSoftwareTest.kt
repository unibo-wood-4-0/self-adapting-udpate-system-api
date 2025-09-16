package it.unibo.osautoupdates.it.unibo.osautoupdates.resolution.software

import arrow.core.nel
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.ds.DoesNothingDS
import it.unibo.osautoupdates.software.ReleasedSoftware.Companion.releasedSoftware
import it.unibo.osautoupdates.software.version.span.VersionSpan.Companion.singleFromString
import it.unibo.osautoupdates.system.oscommand.OsCommand

class ReleasedSoftwareTest :
    ShouldSpec({
        val swName = "VeryCoolSoftware"
        val test = OsCommand("gradlew test")
        val swVersion = singleFromString("1.0.0")
        val swDeploymentStrategy = DoesNothingDS
        val swDependencies =
            setOf("dep1", "dep2")
                .map { name ->
                    releasedSoftware(name, singleFromString("1.0.0")) {
                        dss { +swDeploymentStrategy }
                        validationTests {
                            +test
                        }
                    }
                }.toSet()

        val sw =
            releasedSoftware(swName, swVersion) {
                dss { +swDeploymentStrategy }
                validationTests {
                    +test
                }
                dependencies {
                    swDependencies.forEach { +it }
                }
            }
        should("have the correct attributes") {
            with(sw) {
                name shouldBe swName
                versionSpan shouldBe swVersion
                deploymentStrategies shouldBe swDeploymentStrategy.nel()
                validationTests shouldBe test.nel()
                dependencies shouldBe swDependencies
            }
        }
    })
